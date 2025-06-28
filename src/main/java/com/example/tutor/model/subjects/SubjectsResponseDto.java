package com.example.tutor.model.subjects;

import com.example.tutor.db.entity.HB.HBSubjects;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Builder
@Schema(description = "DTO для ответа по предмету")
public class SubjectsResponseDto {

        @Schema(description = "ID предмета", example = "1")
        Long id;

        @Schema(description = "Алиас", example = "bishkek")
        String alias;

        @Schema(description = "Название на русском", example = "Математика")
        String nameRu;

        @Schema(description = "Название на кыргызском", example = "Математика")
        String nameKy;

        @Schema(description = "Удален", example = "false")
        Boolean deleted;

        @Schema(description = "Дата редактирования", example = "2024-05-20T10:00:00Z")
        Date editedTime;

        @Schema(description = "Дата создания", example = "2024-01-01T12:00:00Z")
        Date createdTime;

        public static SubjectsResponseDto from(HBSubjects subjects) {
            return SubjectsResponseDto.builder()
                    .id(subjects.getId())
                    .alias(subjects.getAlias())
                    .nameRu(subjects.getNameRu())
                    .nameKy(subjects.getNameKy())
                    .deleted(subjects.isDeleted())
                    .editedTime(subjects.getEditedTime())
                    .createdTime(subjects.getCreatedTime())
                    .build();
        }
}
