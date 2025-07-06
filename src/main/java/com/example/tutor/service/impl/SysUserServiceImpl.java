package com.example.tutor.service.impl;

import com.example.tutor.controller.BaseController;
import com.example.tutor.db.entity.AccountActivation;
import com.example.tutor.db.entity.HB.HBCities;
import com.example.tutor.db.entity.HB.UserPhoto;
import com.example.tutor.db.entity.HB.HBSubjects;
import com.example.tutor.db.entity.SubjectExperience;
import com.example.tutor.db.entity.TutorReview;
import com.example.tutor.db.entity.sys.*;
import com.example.tutor.db.repository.*;
import com.example.tutor.db.repository.hb.CitiesRepository;
import com.example.tutor.db.repository.hb.SubjectsRepository;
import com.example.tutor.db.repository.hb.UserPhotoRepository;
import com.example.tutor.db.repository.specification.SysUserSpecification;
import com.example.tutor.db.repository.specification.TutorReviewSpecification;
import com.example.tutor.exception.BadRequestException;
import com.example.tutor.exception.ForbiddenException;
import com.example.tutor.exception.NotFoundException;
import com.example.tutor.exception.TooManyRequestsException;
import com.example.tutor.model.tutorDetails.*;
import com.example.tutor.model.user.PhotoResponseDto;
import com.example.tutor.model.user.filter.UserFilterDto;
import com.example.tutor.model.user.request.AdminChangePasswordRequestDto;
import com.example.tutor.model.user.request.ResetPasswordRequestDto;
import com.example.tutor.model.user.request.SysUserChangePasswordRequestDto;
import com.example.tutor.model.user.request.SysUserRequest;
import com.example.tutor.model.user.response.PageSysUserDtoResponse;
import com.example.tutor.model.user.response.SysUserResponseDto;
import com.example.tutor.service.SysLogRequestService;
import com.example.tutor.service.SysUserService;
import com.example.tutor.util.FileUtils;
import com.example.tutor.util.GsonConfig;
import com.example.tutor.util.MailSender;
import com.google.gson.Gson;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {
    private final SysUserRepository repository;
    private final BaseController baseController;
    private final SysRoleRepository rolesRepository;
    private final SysLogRequestService logService;
    private final MailSender mailSender;

    private final PasswordResetTokenRepository tokenRepository;
    private final TutorDetailsRepository tutorDetailsRepository;
    private final AccountActivationRepository accountActivationRepository;
    private final SubjectExperienceRepository subjectExperienceRepository;
    private final SubjectsRepository subjectsRepository;
    private final TutorEducationRepository tutorEducationRepository;
    private final FileUtils fileUtils;
    private final UserPhotoRepository userPhotoRepository;

    private final Gson gson = GsonConfig.createGson();
    private final CitiesRepository citiesRepository;

    @Override
    public PageSysUserDtoResponse findAll(UserFilterDto filter) {
        Specification<SysUser> spec = new SysUserSpecification(filter);
        Sort sort = Sort.by(Sort.Direction.DESC, "createdTime");

        if (filter.getSortByRate() != null) {
            sort = Sort.by(Sort.Direction.fromString(filter.getSortByRate()), "tutorDetails.rate");
        }

        Pageable pageable = PageRequest.of(BaseController.getPage(filter.getPage()), filter.getSize(), sort);
        Page<SysUser> sysUsersPage = repository.findAll(spec, pageable);

        return PageSysUserDtoResponse.from(sysUsersPage, fileUtils);
    }


    public SysUserResponseDto getById(Long id) {
        SysUser sysUser = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("user.not_found"));

        TutorDetails tutorDetails = null;
        List<TutorEducation> educations = Collections.emptyList();
        List<SubjectExperience> experiences = Collections.emptyList();

        if (sysUser.getTutorDetails() != null) {
            tutorDetails = tutorDetailsRepository.findByIdWithReviews(sysUser.getTutorDetails().getId())
                    .orElseThrow(() -> new NotFoundException("tutor_details.not_found"));

            educations = tutorDetails.getEducations();
            experiences = tutorDetails.getSubjectExperiences();
        }

        return SysUserResponseDto.fromForTutor(sysUser, tutorDetails, educations, experiences, fileUtils);
    }

    @Override
    public SysUser findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("user.not_found"));
    }

    @Override
    public Date getStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    @Override
    @Transactional
    public PhotoResponseDto uploadPhoto(MultipartFile file, HttpServletRequest request) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("error.photo_required");
        }

        SysUser currentUser = getFromContext();

        UserPhoto oldPhoto = currentUser.getUserPhoto();
        if (oldPhoto != null) {
            fileUtils.deleteFileIfExists(oldPhoto.getFilePath());
            fileUtils.deleteFileIfExists(oldPhoto.getFilePathThumb());
            fileUtils.deleteFileIfExists(oldPhoto.getFilePathLarge());

            userPhotoRepository.delete(oldPhoto);
        }

        String savedPath = fileUtils.saveMultipartFileWithResize(file);

        UserPhoto newPhoto = UserPhoto.builder()
                .sysUser(currentUser)
                .filePath(savedPath)
                .filePathThumb("mini/" + savedPath)
                .filePathLarge("large/" + savedPath)
                .contentType(file.getContentType())
                .build();

        UserPhoto savedPhoto = userPhotoRepository.save(newPhoto);

        currentUser.setUserPhoto(savedPhoto);
        repository.save(currentUser);

        logService.saveSuccessToDb(
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                "Uploaded/Updated photo for userId = " + currentUser.getId(),
                request
        );

        return PhotoResponseDto.from(savedPhoto, fileUtils);
    }

    @Override
    @Transactional
    public SysUserResponseDto create(SysUserRequest userDto, HttpServletRequest request) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        TutorDetails detailsSaved = null;
        List<TutorEducation> educations = Collections.emptyList();
        List<SubjectExperience> experiences = Collections.emptyList();

        var roles = rolesRepository.findAllById(userDto.getRoleIds());
        if (roles.size() != userDto.getRoleIds().size()) {
            throw new NotFoundException("error.user.not_found.role");
        }

        SysUser user = SysUser.builder()
                .age(userDto.getAge())
                .telegram(userDto.getTelegram())
                .name(userDto.getName())
                .secondName(userDto.getSecondName())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhone())
                .gender(userDto.getGender())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .roles(new HashSet<>(roles))
                .build();

        repository.save(user);

        boolean isTutor = roles.stream().anyMatch(r -> r.getAlias().equalsIgnoreCase("TUTOR"));
        if (isTutor) {
            HBCities city = citiesRepository.findById(userDto.getTutorDetails().getCityId())
                    .orElseThrow(() -> new NotFoundException("error.city.not_found"));

            TutorDetails details = TutorDetails.builder()
                    .user(user)
                    .price(userDto.getTutorDetails().getPrice())
                    .city(city)
                    .experience(userDto.getTutorDetails().getExperience())
                    .aboutMe(userDto.getTutorDetails().getAboutMe())
                    .online(userDto.getTutorDetails().getOnline())
                    .offline(userDto.getTutorDetails().getOffline())
                    .atTutor(userDto.getTutorDetails().getAtTutor())
                    .address(userDto.getTutorDetails().getAddress())
                    .rate(0.0)
                    .build();
            tutorDetailsRepository.save(details);
            detailsSaved = details;

            if (userDto.getTutorEducations() != null) {
                for (TutorEducationRequestDto eduDto : userDto.getTutorEducations()) {
                    TutorEducation edu = new TutorEducation();
                    edu.setTutor(details);
                    edu.setUniversity(eduDto.getUniversity());
                    edu.setGraduationYear(eduDto.getGraduationYear());
                    tutorEducationRepository.save(edu);
                }
            }

            if (userDto.getSubjectExperiences() != null) {
                for (SubjectExperienceRequestDto dto : userDto.getSubjectExperiences()) {
                    HBSubjects subject = subjectsRepository.findById(dto.getSubjectId())
                            .orElseThrow(() -> new NotFoundException("error.subject.not_found"));

                    if (dto.getExperienceDescriptions() != null) {
                        for (String desc : dto.getExperienceDescriptions()) {
                            SubjectExperience experience = SubjectExperience.builder()
                                    .tutor(details)
                                    .subject(subject)
                                    .experienceDescription(desc)
                                    .build();

                            subjectExperienceRepository.save(experience);
                        }
                    }
                }
            }

            educations = tutorEducationRepository.findByTutor(detailsSaved);
            experiences = subjectExperienceRepository.findByTutor(detailsSaved);

            return SysUserResponseDto.fromForTutor(user, detailsSaved, educations, experiences, fileUtils);
        }

        var activation = new AccountActivation(user);
        activation.setActivated(true);
        accountActivationRepository.save(activation);
       //mailSender.sendActivationLink(user.getEmail(), activation.getActivationCode());

        userDto.setPassword(null);
        logService.saveSuccessToDb(this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                gson.toJson(userDto), request);

        return SysUserResponseDto.from(user, fileUtils);
    }

    @Override
    @Transactional
    public SysUserResponseDto createUserByAdmin(SysUserRequest userDto, HttpServletRequest request) throws MessagingException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        var roles = rolesRepository.findAllById(userDto.getRoleIds());
        if (roles.size() != userDto.getRoleIds().size()) {
            throw new NotFoundException("error.user.not_found.role");
        }

        SysUser user;

        user = SysUser.builder()
                .age(userDto.getAge())
                .name(userDto.getName())
                .secondName(userDto.getSecondName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .temporaryAccessUntilTime(userDto.getTemporaryAccessUntilTime())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhone())
                .gender(userDto.getGender())
                .roles(new HashSet<>(roles))
                .build();


        user.setRoles(new HashSet<>(roles));

        repository.save(user);

        userDto.setPassword(null);
        logService.saveSuccessToDb(
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                gson.toJson(userDto),
                request);
        return SysUserResponseDto.from(user, fileUtils);
    }

    @Override
    public PhotoResponseDto getPhoto(Long userId) {
        SysUser user = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("error.user.not_found"));

        UserPhoto photo = user.getUserPhoto();
        if (photo == null) {
            throw new NotFoundException("error.user.photo_not_found");
        }

        return PhotoResponseDto.from(photo, fileUtils);
    }

    @Override
    @Transactional
    public SysUserResponseDto update(SysUserRequest userDto, HttpServletRequest request) {
        var user = findById(userDto.getId());

        var roles = rolesRepository.findAllById(userDto.getRoleIds());
        if (roles.size() != userDto.getRoleIds().size()) {
            throw new NotFoundException("error.user.not_found.role");
        }

        user.setName(userDto.getName());
        user.setAge(userDto.getAge());
        user.setTelegram(userDto.getTelegram());
        user.setSecondName(userDto.getSecondName());
        user.setTemporaryAccessUntilTime(userDto.getTemporaryAccessUntilTime());
        user.setEditedTime(new Date());
        user.setDeleted(false);
        user.setEmail(userDto.getEmail());
        user.setGender(userDto.getGender());
        user.setPhoneNumber(userDto.getPhone());
        user.setRoles(new HashSet<>(roles));

        boolean isTutor = roles.stream().anyMatch(r -> r.getAlias().equalsIgnoreCase("TUTOR"));
        TutorDetails detailsSaved = null;

        if (isTutor) {
            TutorDetails details = user.getTutorDetails();
            if (details == null) {
                details = new TutorDetails();
                details.setUser(user);
            }

            HBCities city = citiesRepository.findById(userDto.getTutorDetails().getCityId())
                    .orElseThrow(() -> new NotFoundException("error.city.not_found"));

            details.setPrice(userDto.getTutorDetails().getPrice());
            details.setCity(city);
            details.setAboutMe(userDto.getTutorDetails().getAboutMe());
            details.setOnline(userDto.getTutorDetails().getOnline());
            details.setOffline(userDto.getTutorDetails().getOffline());
            details.setAtTutor(userDto.getTutorDetails().getAtTutor());
            details.setAddress(userDto.getTutorDetails().getAddress());
            details.setExperience(userDto.getTutorDetails().getExperience());

            tutorDetailsRepository.save(details);
            detailsSaved = details;

            tutorEducationRepository.deleteAllInBatch(tutorEducationRepository.findByTutor(details));
            subjectExperienceRepository.deleteAllInBatch(subjectExperienceRepository.findByTutor(details));

            if (userDto.getTutorEducations() != null) {
                for (TutorEducationRequestDto eduDto : userDto.getTutorEducations()) {
                    TutorEducation edu = new TutorEducation();
                    edu.setTutor(details);
                    edu.setUniversity(eduDto.getUniversity());
                    edu.setGraduationYear(eduDto.getGraduationYear());
                    tutorEducationRepository.save(edu);
                }
            }

            if (userDto.getSubjectExperiences() != null) {
                for (SubjectExperienceRequestDto dto : userDto.getSubjectExperiences()) {
                    HBSubjects subject = subjectsRepository.findById(dto.getSubjectId())
                            .orElseThrow(() -> new NotFoundException("error.subject.not_found"));

                    if (dto.getExperienceDescriptions() != null) {
                        for (String desc : dto.getExperienceDescriptions()) {
                            SubjectExperience experience = SubjectExperience.builder()
                                    .tutor(details)
                                    .subject(subject)
                                    .experienceDescription(desc)
                                    .build();
                            subjectExperienceRepository.save(experience);
                        }
                    }
                }
            }
        }

        var result = repository.save(user);

        logService.saveSuccessToDb(this.getClass().getSimpleName(), "update", gson.toJson(userDto), request);

        userDto.setPassword(null);

        if (isTutor) {
            List<TutorEducation> educations = tutorEducationRepository.findByTutor(detailsSaved);
            List<SubjectExperience> experiences = subjectExperienceRepository.findByTutor(detailsSaved);
            return SysUserResponseDto.fromForTutor(result, detailsSaved, educations, experiences, fileUtils);
        }

        return SysUserResponseDto.from(result, fileUtils);
    }

    @Override
    public void delete(Long id, HttpServletRequest request) {
        SysUser currentUser = getFromContext();
        SysUser targetUser = findById(id);

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getAlias().equalsIgnoreCase("ADMIN"));

        if (!isAdmin && !currentUser.getId().equals(id)) {
            throw new ForbiddenException("error.user.delete.not_author_or_admin");
        }

        targetUser.setDeleted(true);
        repository.save(targetUser);

        logService.saveSuccessToDb(
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                gson.toJson(id),
                request
        );
    }

    @Override
    public SysUserResponseDto changePassword(SysUserChangePasswordRequestDto userDto, HttpServletRequest request) {
        var result = changePassword(userDto.getEmail(), userDto.getPassword());

        logService.saveSuccessToDb(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), String.format("user %d successfully changed the password", result.getId()),
                request);

        return SysUserResponseDto.from(result, fileUtils);
    }



    @Override
    public SysUserResponseDto adminChangePassword(AdminChangePasswordRequestDto changePasswordRequestDto, HttpServletRequest request) {
        var result = changePassword(changePasswordRequestDto.getEmail(), changePasswordRequestDto.getPassword());

        logService.saveSuccessToDb(this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                String.format("Super admin change password user %d successfully changed the password", result.getId()),
                request);

        return SysUserResponseDto.from(result, fileUtils);

    }

    @Override
    public SysUserResponseDto changePasswordCurrentUser(SysUserChangePasswordRequestDto dto, HttpServletRequest request) {
        SysUser user = getFromContext();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new ForbiddenException("error.password.incorrect_current");
        }

        if (encoder.matches(dto.getPassword(), user.getPassword()) ||
                (user.getLastPassword() != null && encoder.matches(dto.getPassword(), user.getLastPassword())) ||
                (user.getSecondLastPassword() != null && encoder.matches(dto.getPassword(), user.getSecondLastPassword())) ||
                (user.getThirdLastPassword() != null && encoder.matches(dto.getPassword(), user.getThirdLastPassword()))
        ) {
            throw new BadRequestException("error.password.same_as_old");
        }

        String oldPassword = user.getPassword();

        user.setPassword(encoder.encode(dto.getPassword()));
        user.setPasswordLastChangeTime(new Date());
        user.setPasswordChangeNextLogon(false);
        user.setEditedTime(new Date());

        if (user.getLastPassword() != null) {
            if (user.getSecondLastPassword() != null) {
                user.setThirdLastPassword(user.getSecondLastPassword());
            }
            user.setSecondLastPassword(user.getLastPassword());
        }
        user.setLastPassword(oldPassword);

        repository.save(user);

        logService.saveSuccessToDb(
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                gson.toJson(dto), request
        );

        return SysUserResponseDto.from(user, fileUtils);
    }

    private SysUser changePassword(String email, String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        var user = repository.findByEmail(email).orElseThrow(() ->
                new NotFoundException("error.user.not_found.pin"));

        String currentPassword = user.getPassword();

        user.setPassword(passwordEncoder.encode(password));
        user.setPasswordLastChangeTime(new Date());
        user.setPasswordChangeNextLogon(false);
        user.setEditedTime(new Date());

        if (user.getLastPassword() != null) {
            if (user.getSecondLastPassword() != null) {
                user.setThirdLastPassword(user.getSecondLastPassword());
            }
            user.setSecondLastPassword(user.getLastPassword());
        }

        user.setLastPassword(currentPassword);

        return repository.save(user);
    }

    @Override
    public SysUserResponseDto getByJWT(HttpServletRequest request) {
        var user = baseController.getUserFromToken(request).orElseThrow(() -> new NotFoundException("User not found."));
        return SysUserResponseDto.from(user, fileUtils);
    }

    @Override
    public SysUser ban(Long userId, HttpServletRequest request) {
        var user = findById(userId);
        user.setBanned(true);

        repository.save(user);

        logService.saveSuccessToDb(
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                gson.toJson(userId),
                request);
        return user;
    }

    @Override
    public SysUser unban(Long userId, HttpServletRequest request) {
        var user = findById(userId);

        user.setBanned(false);
        user.setFailedLoginAttempts(0);

        repository.save(user);

        logService.saveSuccessToDb(
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                gson.toJson(userId),
                request);

        return user;
    }

    @Override
    public void updateTheNumberOfFailedLogins(String username) {
        Optional<SysUser> userOptional = repository.findByEmail(username);
        if (userOptional.isPresent()) {
            SysUser user = userOptional.get();
            if (user.getFailedLoginAttempts() == 4) {
                user.setBanned(true);
                repository.save(user);
            } else {
                user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
                repository.save(user);
            }

        }

    }

    @Override
    public SysUserResponseDto activateUser(UUID activationCode) {
        var activation = accountActivationRepository.findByActivationCode(activationCode)
                .orElseThrow(() -> new NotFoundException("error.activation_code.not_found"));

        if (activation.isActivated()) {
            throw new NotFoundException("error.activation_code.already_activated");
        }

        if (activation.getExpiresAt().before(new Date())) {
            throw new NotFoundException("error.activation_code.is_expires");
        }

        var user = activation.getUser();
        user.setEmailVerified(true);
        repository.save(user);
        activation.setActivated(true);
        activation.setEditedTime(new Date());
        accountActivationRepository.save(activation);
        return SysUserResponseDto.from(user, fileUtils);
    }

    @Override
    public void sendActivationLink(Long userId, HttpServletRequest request) throws MessagingException {
        var user = findById(userId);
        if (user.isEmailVerified()) {
            throw new NotFoundException("error.user.already_activated");
        }
        var allLinks = accountActivationRepository.findAllByUserId(userId);
        accountActivationRepository.deleteAll(allLinks);
        var accountActivation = accountActivationRepository.save(new AccountActivation(user));

        mailSender.sendActivationLink(user.getEmail(), accountActivation.getActivationCode());

        logService.saveSuccessToDb(
                this.getClass().getSimpleName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                gson.toJson(userId),
                request);

    }

    public void processForgotPassword(String email) throws MessagingException {
        SysUser user = repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("error.user.not_found"));

        Date startOfDay = getStartOfDay();
        List<PasswordResetToken> tokensToday = tokenRepository.findByUserAndExpiryDateAfter(user, startOfDay);
        if (tokensToday.size() >= 3) {
            throw new TooManyRequestsException("error.password.reset_limit");
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        tokenRepository.save(resetToken);

        Integer passwordLength = user.getRoles().stream()
                .map(SysRole::getPasswordLength)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(18);

        mailSender.sendPasswordResetLink(user.getEmail(), token, passwordLength);
    }

    public void validateToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("error.token.not_found"));
        if (resetToken.getExpiryDate().before(new Date())) {
            throw new BadRequestException("error.token.expired_or_invalid");
        }
    }

    public void resetPassword(ResetPasswordRequestDto requestDto) {
        PasswordResetToken resetToken = tokenRepository.findByToken(requestDto.getToken())
                .orElseThrow(() -> new NotFoundException("error.token.not_found"));

        if (resetToken.getExpiryDate().before(new Date())) {
            throw new BadRequestException("error.token.expired_or_invalid");
        }

        if (requestDto.getNewPassword() == null) {
            throw new BadRequestException("error.password.too_short");
        }

        SysUser user = resetToken.getUser();
        changePassword(user.getEmail(), requestDto.getNewPassword());
        tokenRepository.delete(resetToken);
    }


    @Override
    public SysUser getFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken)) {

            String username = authentication.getName();

            return repository.findByEmail(username)
                    .orElseThrow(() -> new NotFoundException("error.user.not_found"));
        }

        throw new ForbiddenException("error.user.not_authenticated");
    }
}
