package com.example.tutor.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BasePageRequest {
    @Schema(description = "Номер страницы", example = "1")
    Integer page = 1;

    @Schema(description = "Размер страницы", example = "15")
    Integer size = 15;
}
