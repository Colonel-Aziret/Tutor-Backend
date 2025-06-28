package com.example.tutor.model.cities;

import com.example.tutor.constraint.UniqueAlias;
import com.example.tutor.db.repository.hb.CitiesRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO для запроса города")
@UniqueAlias(repository = CitiesRepository.class)
public class CitiesRequestDto {

    @Schema(description = "ID города", example = "1")
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
