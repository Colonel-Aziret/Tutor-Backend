package com.example.tutor.model.tutorDetails;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO для ответа на комментарий")
public class TutorReplyRequestDto {

    @Schema(description = "ID комментария, на который отвечают", required = true)
    @NotNull
    Long parentId;

    @Schema(description = "ID тьютора, к которому относится комментарий", required = true)
    @NotNull
    Long tutorId;

    @Schema(description = "Текст ответа", required = true)
    @NotBlank
    String comment;
}
