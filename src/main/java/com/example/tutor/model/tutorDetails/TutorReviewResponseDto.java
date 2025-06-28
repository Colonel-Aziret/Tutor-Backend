package com.example.tutor.model.tutorDetails;

import com.example.tutor.db.entity.TutorReview;
import com.example.tutor.model.user.PhotoResponseDto;
import com.example.tutor.util.FileUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@Schema(description = "Отзыв о тьюторе")
public class TutorReviewResponseDto {

    @Schema(description = "Фото автора отзыва")
    PhotoResponseDto authorPhoto;

    @Schema(description = "Автор")
    String authorName;

    @Schema(description = "Комментарий")
    String comment;

    @Schema(description = "Оценка")
    Integer rating;

    @Schema(description = "Дата создания")
    Date createdTime;


    public static TutorReviewResponseDto from(TutorReview review, FileUtils fileUtils) {
        return TutorReviewResponseDto.builder()
                .authorName(review.getAuthor().getSecondName() + " " + review.getAuthor().getName())
                .authorPhoto(review.getAuthor().getUserPhoto() != null
                        ? PhotoResponseDto.from(review.getAuthor().getUserPhoto(), fileUtils)
                        : null)
                .comment(review.getComment())
                .rating(review.getRating())
                .createdTime(review.getCreatedTime())
                .build();
    }
}
