package com.example.tutor.service.impl;

import com.example.tutor.controller.BaseController;
import com.example.tutor.db.entity.TutorReview;
import com.example.tutor.db.entity.sys.SysUser;
import com.example.tutor.db.entity.sys.TutorDetails;
import com.example.tutor.db.repository.SysUserRepository;
import com.example.tutor.db.repository.TutorDetailsRepository;
import com.example.tutor.db.repository.TutorReviewRepository;
import com.example.tutor.db.repository.specification.TutorReviewSpecification;
import com.example.tutor.exception.ForbiddenException;
import com.example.tutor.exception.NotFoundException;
import com.example.tutor.model.tutorDetails.PageTutorReviewResponseDto;
import com.example.tutor.model.tutorDetails.TutorReviewFilterDto;
import com.example.tutor.model.tutorDetails.TutorReviewRequestDto;
import com.example.tutor.service.ReviewService;
import com.example.tutor.service.SysUserService;
import com.example.tutor.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final SysUserService sysUserService;
    private final TutorReviewRepository tutorReviewRepository;
    private final SysUserRepository sysUserRepository;
    private final FileUtils fileUtils;
    private final TutorDetailsRepository tutorDetailsRepository;

    @Override
    public PageTutorReviewResponseDto getAllComments(TutorReviewFilterDto filter) {
        Specification<TutorReview> specification = new TutorReviewSpecification(filter);

        Pageable pageable = PageRequest.of(
                BaseController.getPage(filter.getPage()),
                filter.getSize(),
                Sort.by(Sort.Direction.DESC, "createdTime")
        );

        Page<TutorReview> page = tutorReviewRepository.findAll(specification, pageable);
        return PageTutorReviewResponseDto.from(page, fileUtils);
    }


    @Transactional
    public void unbanUserFromComments(Long userId) {
        SysUser user = sysUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("error.user.not_found"));

        if (user.isCanComment()) {
            throw new IllegalStateException("Пользователь уже имеет право комментировать");
        }

        user.setCanComment(true);
        sysUserRepository.save(user);
    }

    @Transactional
    public void banUserFromComments(Long userId) {
        SysUser user = sysUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("error.user.not_found"));
        user.setCanComment(false);
        sysUserRepository.save(user);

        List<TutorReview> reviews = tutorReviewRepository.findByAuthor(user);
        Set<TutorDetails> affectedTutors = reviews.stream()
                .map(TutorReview::getTutor)
                .collect(Collectors.toSet());

        tutorReviewRepository.deleteAllByAuthorId(userId);

        for (TutorDetails tutor : affectedTutors) {
            double avg = tutorReviewRepository.findByTutor(tutor).stream()
                    .mapToInt(TutorReview::getRating)
                    .average()
                    .orElse(0);
            tutor.setRate(avg);
            tutorDetailsRepository.save(tutor);
        }
    }

    @Transactional
    public void addReview(TutorReviewRequestDto requestDto) {
        SysUser author = sysUserService.getFromContext();
        TutorDetails tutor = tutorDetailsRepository.findByUserId(requestDto.getTutorId())
                .orElseThrow(() -> new NotFoundException("tutor.not_found"));

        if (!author.isCanComment()) {
            throw new AccessDeniedException("Вы не можете оставлять комментарии");
        }

        TutorReview review = TutorReview.builder()
                .tutor(tutor)
                .author(author)
                .comment(requestDto.getComment())
                .rating(requestDto.getRating())
                .build();

        tutorReviewRepository.save(review);

        List<TutorReview> all = tutorReviewRepository.findByTutor(tutor);
        double avg = all.stream().mapToInt(TutorReview::getRating).average().orElse(0);
        tutor.setRate(avg);

        tutorDetailsRepository.save(tutor);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        SysUser currentUser = sysUserService.getFromContext();

        TutorReview review = tutorReviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("tutor_review.not_found"));

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getAlias().equalsIgnoreCase("SUPER_ADMIN"));

        if (!isAdmin && !review.getAuthor().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("error.review.delete.not_author");
        }

        TutorDetails tutor = review.getTutor();

        tutor.getReviews().remove(review);

        tutorReviewRepository.delete(review);

        List<TutorReview> remainingReviews = tutorReviewRepository.findByTutor(tutor);
        double avg = remainingReviews.stream()
                .mapToInt(TutorReview::getRating)
                .average()
                .orElse(0);

        tutor.setRate(avg);
        tutorDetailsRepository.save(tutor);
    }
}
