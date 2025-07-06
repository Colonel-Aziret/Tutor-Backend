package com.example.tutor.model.tutorDetails;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO отзыва о тьюторе")
public class TutorReviewRequestDto {

    @Schema(description = "ID тьютора", example = "42")
    @NotNull
    private Long tutorId;

    @Schema(description = "ID родительского комментария, если это ответ на него")
    Long parentId;

    @Schema(description = "Комментарий", example = "Очень хороший преподаватель!", maxLength = 1000)
    @NotBlank
    private String comment;

    @Schema(description = "Оценка от 1 до 5", example = "5", minimum = "1", maximum = "5")
    @Min(1)
    @Max(5)
    private Integer rating;
}
