package com.example.tutor.model.user.response;

import com.example.tutor.db.entity.SubjectExperience;
import com.example.tutor.db.entity.sys.TutorDetails;
import com.example.tutor.db.entity.sys.TutorEducation;
import com.example.tutor.model.tutorDetails.SubjectExperienceResponseDto;
import com.example.tutor.model.tutorDetails.TutorDetailsResponseDto;
import com.example.tutor.model.tutorDetails.TutorEducationResponseDto;
import com.example.tutor.model.user.PhotoResponseDto;
import com.example.tutor.util.FileUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import io.swagger.v3.oas.annotations.media.Schema;
import com.example.tutor.db.entity.sys.SysUser;
import com.example.tutor.db.enums.Gender;
import com.example.tutor.model.role.response.SysRoleResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;


@Data
@Builder
@Schema(description = "DTO для ответа пользователя")
public class SysUserResponseDto {

    @Schema(description = "Идентификатор пользователя", example = "1")
    Long id;

    @Schema(description = "Флаг блокировки пользователя")
    Boolean isBanned;

    @Schema(description = "ПИН", example = "1")
    String pin;

    @Schema(description = "Фамилия", example = "Тестов")
    String secondName;

    @Schema(description = "Имя", example = "Тест")
    String name;

    @Schema(description = "Возраст", example = "18")
    Integer age;

    @Schema(description = "Отчество", example = "Тестович")
    String patronymic;

    @Schema(description = "SUB", example = "sub123")
    String sub;

    @Schema(description = "Гражданство", example = "KG")
    String citizenship;

    @Schema(description = "Пол")
    Gender gender;

    @Schema(description = "Дата рождения", example = "1990-01-01")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date birthdate;

    @Schema(description = "Последнее время авторизации", example = "2024-11-18 14:30:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    Date lastLogin;

    @Schema(description = "Удален", example = "false")
    Boolean deleted;

    @Schema(description = "Требуется смена пароля при следующей авторизации", example = "false")
    Boolean passwordChangeNextLogon;

    @Schema(description = "Время последнего изменения пароля", example = "2024-11-18 14:30:00")
    Date passwordLastChangeTime;

    @Schema(description = "Временный доступ до времени", example = "2024-11-18 14:30:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    Date temporaryAccessUntilTime;

    @Schema(description = "Время редактирования", example = "2024-11-18 14:30:00")
    Date editedTime;

    @Schema(description = "Время создания записи", example = "2024-11-18 14:30:00")
    Date createdTime;

    @Schema(description = "Список ролей", implementation = SysRoleResponseDto.class)
    List<SysRoleResponseDto> roles;

    @Schema(description = "Код страны", example = "+996")
    String countryCode;

    @Schema(description = "Номер телефона")
    String phone;

    @Schema(description = "Email")
    String email;

    @Schema(description = "Email подтверждён")
    Boolean emailVerified;

    @Schema(description = "Телефон подтверждён")
    Boolean phoneNumberVerified;

    @Schema(description = "Информация о тьюторе")
    TutorDetailsResponseDto tutorDetails;

    @Schema(description = "Образование тьютора")
    List<TutorEducationResponseDto> tutorEducations;

    @Schema(description = "Предметы и опыт преподавания")
    List<SubjectExperienceResponseDto> subjectExperiences;

    @Schema(description = "Фото пользователя")
    PhotoResponseDto userPhoto;


    public static SysUserResponseDto fromForTutor(SysUser user,
                                                  TutorDetails details,
                                                  List<TutorEducation> educationList,
                                                  List<SubjectExperience> experienceList, FileUtils fileUtils) {
        if (user == null) {
            return null;
        }
        PhotoResponseDto photoDto = user.getUserPhoto() != null
                ? PhotoResponseDto.from(user.getUserPhoto(), fileUtils)
                : null;

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        String fullPhone = user.getPhoneNumber();

        String countryCode;
        String nationalNumber;

        try {
            Phonenumber.PhoneNumber parsed = phoneUtil.parse(fullPhone, null);
            countryCode = "+" + parsed.getCountryCode();
            nationalNumber = String.valueOf(parsed.getNationalNumber());
        } catch (NumberParseException e) {
            countryCode = null;
            nationalNumber = null;
        }
        List<SysRoleResponseDto> roleResponses = user.getRoles() != null ? user.getRoles().stream()
                .map(SysRoleResponseDto::from)
                .toList() : Collections.emptyList();

        return SysUserResponseDto.builder()
                .id(user.getId())
                .age(user.getAge())
                .isBanned(user.isBanned())
                .secondName(user.getSecondName())
                .name(user.getName())
                .gender(user.getGender())
                .lastLogin(user.getLastLogin())
                .deleted(user.isDeleted())
                .passwordChangeNextLogon(user.getPasswordChangeNextLogon())
                .passwordLastChangeTime(user.getPasswordLastChangeTime())
                .temporaryAccessUntilTime(user.getTemporaryAccessUntilTime())
                .editedTime(user.getEditedTime())
                .createdTime(user.getCreatedTime())
                .roles(roleResponses)
                .phone(nationalNumber)
                .countryCode(countryCode)
                .phoneNumberVerified(user.getPhoneNumberVerified())
                .email(user.getEmail())
                .emailVerified(user.isEmailVerified())
                .tutorDetails(details != null ? TutorDetailsResponseDto.from(details, fileUtils) : null)
                .tutorEducations(educationList != null ? educationList.stream()
                        .map(TutorEducationResponseDto::from).collect(Collectors.toList()) : null)
                .subjectExperiences(experienceList != null ? groupSubjectExperiences(experienceList) : null)
                .userPhoto(photoDto)
                .build();
    }

    private static List<SubjectExperienceResponseDto> groupSubjectExperiences(List<SubjectExperience> experiences) {
        Map<Long, SubjectExperienceResponseDto> grouped = new HashMap<>();

        for (SubjectExperience exp : experiences) {
            Long subjectId = exp.getSubject().getId();

            grouped.computeIfAbsent(subjectId, id -> SubjectExperienceResponseDto.builder()
                    .subjectId(subjectId)
                    .subjectName(exp.getSubject().getNameRu())
                    .experienceDescriptions(new ArrayList<>())
                    .build()
            ).getExperienceDescriptions().add(exp.getExperienceDescription());
        }

        return new ArrayList<>(grouped.values());
    }

    public static SysUserResponseDto from(SysUser user, FileUtils fileUtils) {
        if (user == null) {
            return null;
        }

        PhotoResponseDto photoDto = user.getUserPhoto() != null
                ? PhotoResponseDto.from(user.getUserPhoto(), fileUtils)
                : null;

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        String fullPhone = user.getPhoneNumber();

        String countryCode;
        String nationalNumber;

        try {
            Phonenumber.PhoneNumber parsed = phoneUtil.parse(fullPhone, null);
            countryCode = "+" + parsed.getCountryCode();
            nationalNumber = String.valueOf(parsed.getNationalNumber());
        } catch (NumberParseException e) {
            countryCode = null;
            nationalNumber = null;
        }
        List<SysRoleResponseDto> roleResponses = user.getRoles() != null ? user.getRoles().stream()
                .map(SysRoleResponseDto::from)
                .toList() : Collections.emptyList();

        return SysUserResponseDto.builder()
                .id(user.getId())
                .isBanned(user.isBanned())
                .secondName(user.getSecondName())
                .name(user.getName())
                .gender(user.getGender())
                .age(user.getAge())
                .lastLogin(user.getLastLogin())
                .deleted(user.isDeleted())
                .passwordChangeNextLogon(user.getPasswordChangeNextLogon())
                .passwordLastChangeTime(user.getPasswordLastChangeTime())
                .temporaryAccessUntilTime(user.getTemporaryAccessUntilTime())
                .editedTime(user.getEditedTime())
                .createdTime(user.getCreatedTime())
                .roles(roleResponses)
                .phone(nationalNumber)
                .countryCode(countryCode)
                .phoneNumberVerified(user.getPhoneNumberVerified())
                .email(user.getEmail())
                .emailVerified(user.isEmailVerified())
                .userPhoto(photoDto)
                .build();
    }

    public static SysUserResponseDto fromWithoutNumber(SysUser user, FileUtils fileUtils) {
        if (user == null) {
            return null;
        }
        PhotoResponseDto photoDto = user.getUserPhoto() != null
                ? PhotoResponseDto.from(user.getUserPhoto(), fileUtils)
                : null;

        List<SysRoleResponseDto> roleResponses = user.getRoles() != null ? user.getRoles().stream()
                .map(SysRoleResponseDto::from)
                .collect(Collectors.toList()) : Collections.emptyList();

        return SysUserResponseDto.builder()
                .id(user.getId())
                .isBanned(user.isBanned())
                .emailVerified(user.isEmailVerified())
                .email(user.getEmail())
                .emailVerified(user.isEmailVerified())
                .secondName(user.getSecondName())
                .name(user.getName())
                .gender(user.getGender() != null ? user.getGender() : null)
                .age(user.getAge())
                .lastLogin(user.getLastLogin())
                .deleted(user.isDeleted())
                .passwordChangeNextLogon(user.getPasswordChangeNextLogon())
                .passwordLastChangeTime(user.getPasswordLastChangeTime())
                .temporaryAccessUntilTime(user.getTemporaryAccessUntilTime())
                .editedTime(user.getEditedTime())
                .createdTime(user.getCreatedTime())
                .roles(roleResponses)
                .phoneNumberVerified(user.getPhoneNumberVerified())
                .userPhoto(photoDto)
                .build();
    }
}
