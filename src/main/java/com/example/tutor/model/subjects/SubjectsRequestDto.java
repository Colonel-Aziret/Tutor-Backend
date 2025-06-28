package com.example.tutor.model.subjects;

import com.example.tutor.constraint.UniqueAlias;
import com.example.tutor.db.repository.hb.CitiesRepository;
import com.example.tutor.db.repository.hb.SubjectsRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
@Schema(description = "DTO для запроса предмета")
@UniqueAlias(repository = SubjectsRepository.class)
public class SubjectsRequestDto {

        @Schema(description = "ID предмета", example = "1")
        Long id;

        @NotBlank(message = "error.valid.alias")
        @Schema(description = "Алиас", example = "bishkek")
        String alias;

        @NotBlank(message = "error.valid.name_ru")
        @Schema(description = "Название на русском", example = "Бишкек")
        String nameRu;

        @NotBlank(message = "error.valid.name_ky")
        @Schema(description = "Название на кыргызском", example = "Бишкек")
        String nameKy;
    }

