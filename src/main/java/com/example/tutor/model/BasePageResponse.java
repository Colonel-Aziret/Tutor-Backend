package com.example.tutor.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BasePageResponse {
    @Schema(description = "Текущая страница", example = "1")
    private int page;

    @Schema(description = "Размер страницы", example = "1")
    private int size;

    @Schema(description = "Общее количество элементов", example = "5")
    private long totalElements;

    @Schema(description = "Общее количество страниц", example = "5")
    private int totalPages;
}
