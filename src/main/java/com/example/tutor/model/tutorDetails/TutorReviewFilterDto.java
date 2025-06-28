package com.example.tutor.model.tutorDetails;

import com.example.tutor.model.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Фильтр для отзывов")
public class TutorReviewFilterDto extends BasePageRequest {

    @Schema(description = "ID пользователя-тьютора (userId)", example = "123")
    private Long tutorId;
}
