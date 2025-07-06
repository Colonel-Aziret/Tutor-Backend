package com.example.tutor.service;

import com.example.tutor.model.tutorDetails.PageTutorReviewResponseDto;
import com.example.tutor.model.tutorDetails.TutorReplyRequestDto;
import com.example.tutor.model.tutorDetails.TutorReviewFilterDto;
import com.example.tutor.model.tutorDetails.TutorReviewRequestDto;

public interface ReviewService {
    void addReview(TutorReviewRequestDto requestDto);

    void replyToComment(TutorReplyRequestDto requestDto);

    void banUserFromComments(Long userId);

    void unbanUserFromComments(Long userId);

    void deleteReview(Long reviewId);

    PageTutorReviewResponseDto getAllComments(TutorReviewFilterDto filter);
}
