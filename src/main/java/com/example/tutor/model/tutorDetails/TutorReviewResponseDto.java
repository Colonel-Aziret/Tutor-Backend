package com.example.tutor.model.tutorDetails;

import com.example.tutor.db.entity.TutorReview;
import com.example.tutor.model.user.PhotoResponseDto;
import com.example.tutor.util.FileUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@Schema(description = "Отзыв о тьюторе")
public class TutorReviewResponseDto {

    @Schema(description = "ID комментария")
    Long id;

    @Schema(description = "ID родительского комментария (если это ответ)")
    Long parentId;

    @Schema(description = "Фото автора отзыва")
    PhotoResponseDto authorPhoto;

    @Schema(description = "Автор")
    String authorName;

    @Schema(description = "Комментарий")
    String comment;

    @Schema(description = "Оценка (только для корневых комментариев)")
    Integer rating;

    @Schema(description = "Дата создания")
    Date createdTime;

    @Schema(description = "Ответы на этот комментарий")
    List<TutorReviewResponseDto> replies;

    public static TutorReviewResponseDto from(TutorReview review, FileUtils fileUtils, List<TutorReview> allReviews) {
        return TutorReviewResponseDto.builder()
                .id(review.getId())
                .parentId(review.getParent() != null ? review.getParent().getId() : null)
                .authorName(review.getAuthor().getSecondName() + " " + review.getAuthor().getName())
                .authorPhoto(review.getAuthor().getUserPhoto() != null
                        ? PhotoResponseDto.from(review.getAuthor().getUserPhoto(), fileUtils)
                        : null)
                .comment(review.getComment())
                .rating(review.getParent() == null ? review.getRating() : null)
                .createdTime(review.getCreatedTime())
                .replies(allReviews.stream()
                        .filter(r -> r.getParent() != null && r.getParent().getId().equals(review.getId()))
                        .map(r -> from(r, fileUtils, allReviews))
                        .collect(Collectors.toList()))
                .build();
    }
}
