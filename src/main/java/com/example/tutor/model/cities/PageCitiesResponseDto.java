package com.example.tutor.model.cities;

import com.example.tutor.db.entity.HB.HBCities;
import com.example.tutor.model.BasePageResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Schema(description = "Ответ с пагинацией для списка городов")
public class PageCitiesResponseDto extends BasePageResponse {

    @Schema(description = "Контент (список городов)", implementation = CitiesResponseDto.class)
    private List<CitiesResponseDto> content;

    public static PageCitiesResponseDto from(Page<HBCities> page) {
        return PageCitiesResponseDto.builder()
                .content(page.getContent().stream()
                        .map(CitiesResponseDto::from)
                        .collect(Collectors.toList()))
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
