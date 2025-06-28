package com.example.tutor.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для ответа файла")
public class FileUrlResponse {
    @Schema(description = "Ссылка", example = "/test.jpg")
    String url;
    @Schema(description = "Расширение", example = "jpg")
    String ext;
    @Schema(description = "Наименование", example = "test")
    String name;
    @Schema(description = "Идентификатор", example = "1")
    Long id;
}
