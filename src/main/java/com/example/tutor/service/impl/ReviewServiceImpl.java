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
import com.example.tutor.model.tutorDetails.*;
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
        Specification<TutorReview> baseSpec = new TutorReviewSpecification(filter);

        List<TutorReview> allReviews = tutorReviewRepository.findAll(baseSpec);

        Specification<TutorReview> rootOnlySpec = baseSpec.and((root, query, cb) ->
                cb.isNull(root.get("parent"))
        );

        Pageable pageable = PageRequest.of(
                BaseController.getPage(filter.getPage()),
                filter.getSize(),
                Sort.by(Sort.Direction.DESC, "createdTime")
        );

        Page<TutorReview> rootPage = tutorReviewRepository.findAll(rootOnlySpec, pageable);

        List<TutorReviewResponseDto> rootDtos = rootPage.getContent().stream()
                .map(r -> TutorReviewResponseDto.from(r, fileUtils, allReviews))
                .collect(Collectors.toList());

        return PageTutorReviewResponseDto.builder()
                .totalPages(rootPage.getTotalPages())
                .totalElements(rootPage.getTotalElements())
                .currentPage(rootPage.getNumber())
                .size(rootPage.getSize())
                .content(rootDtos)
                .build();
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

        if (!author.isCanComment()) {
            throw new AccessDeniedException("Вы не можете оставлять комментарии");
        }

        TutorDetails tutor = tutorDetailsRepository.findByUserId(requestDto.getTutorId())
                .orElseThrow(() -> new NotFoundException("tutor.not_found"));

        boolean isSelfReview = author.getId().equals(tutor.getUser().getId());
        boolean isTutor = author.getRoles().stream()
                .anyMatch(r -> r.getAlias().equalsIgnoreCase("TUTOR"));

        if (isTutor && !isSelfReview) {
            throw new AccessDeniedException("Тьютор может оставлять комментарии только у себя");
        }

        if (requestDto.getParentId() != null) {
            throw new IllegalArgumentException("Для ответа используйте отдельный endpoint /comment/reply");
        }

        Integer rating = null;
        if (isSelfReview) {
            if (requestDto.getRating() != null) {
                throw new IllegalArgumentException("Тьютору нельзя ставить себе рейтинг");
            }
        } else {
            if (requestDto.getRating() == null || requestDto.getRating() < 1 || requestDto.getRating() > 5) {
                throw new IllegalArgumentException("Оценка обязательна и должна быть от 1 до 5");
            }
            rating = requestDto.getRating();
        }

        TutorReview review = TutorReview.builder()
                .tutor(tutor)
                .author(author)
                .comment(requestDto.getComment())
                .rating(rating)
                .parent(null)
                .build();

        tutorReviewRepository.save(review);

        List<TutorReview> rootReviews = tutorReviewRepository.findByTutor(tutor).stream()
                .filter(r -> r.getParent() == null && r.getRating() != null)
                .toList();

        double avg = rootReviews.stream()
                .mapToInt(TutorReview::getRating)
                .average()
                .orElse(0);

        tutor.setRate(avg);
        tutorDetailsRepository.save(tutor);
    }

    @Transactional
    public void replyToComment(TutorReplyRequestDto requestDto) {
        SysUser author = sysUserService.getFromContext();

        if (!author.isCanComment()) {
            throw new AccessDeniedException("Вы не можете оставлять комментарии");
        }

        TutorDetails tutor = tutorDetailsRepository.findByUserId(requestDto.getTutorId())
                .orElseThrow(() -> new NotFoundException("tutor.not_found"));

        TutorReview parent = tutorReviewRepository.findById(requestDto.getParentId())
                .orElseThrow(() -> new NotFoundException("parent_comment.not_found"));

        TutorReview reply = TutorReview.builder()
                .tutor(tutor)
                .author(author)
                .comment(requestDto.getComment())
                .parent(parent)
                .build();

        tutorReviewRepository.save(reply);
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
