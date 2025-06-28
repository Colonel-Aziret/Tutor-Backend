package com.example.tutor.model.cities;

import com.example.tutor.db.entity.HB.HBCities;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@Schema(description = "DTO для ответа по городу")
public class CitiesResponseDto {

    @Schema(description = "ID города", example = "1")
    Long id;

    @Schema(description = "Алиас", example = "bishkek")
    String alias;

    @Schema(description = "Название на русском", example = "Бишкек")
    String nameRu;

    @Schema(description = "Название на кыргызском", example = "Бишкек")
    String nameKy;

    @Schema(description = "Удален", example = "false")
    Boolean deleted;

    @Schema(description = "Дата редактирования", example = "2024-05-20T10:00:00Z")
    Date editedTime;

    @Schema(description = "Дата создания", example = "2024-01-01T12:00:00Z")
    Date createdTime;

    public static CitiesResponseDto from(HBCities city) {
        return CitiesResponseDto.builder()
                .id(city.getId())
                .alias(city.getAlias())
                .nameRu(city.getNameRu())
                .nameKy(city.getNameKy())
                .deleted(city.isDeleted())
                .editedTime(city.getEditedTime())
                .createdTime(city.getCreatedTime())
                .build();
    }
}
