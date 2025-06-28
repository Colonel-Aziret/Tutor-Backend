package com.example.tutor.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для запроса для файла")
public class FileBodyRequest {
    @NotBlank(message = "error.file_name.not_blank") //Наименование файла не может быть пустым
    @Size(min = 1, max = 90, message = "error.valid.file_name.size_should_min_max")
    //Размер наименование должен быть в диапазоне от минимального до максимального
    @Pattern(regexp = "^[^<>]+$", message = "error.valid.file_name.unacceptable.format")
    //Наименование имеет неприемлемый формат
    @Schema(description = "Наименование")
    String fileName = "";

    @NotBlank(message = "error.file_body.not_blank") //Тело файла не может быть пустым
    @Pattern(
            regexp = "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{4})$",
            message = "error.valid.file_body.unacceptable.format" //Тело файла имеет неприемлемый формат
    )
    @Schema(description = "Тело")
    String fileBody = "";

    @NotBlank(message = "error.valid.file_ext.not_blank") //Расширение файла не может быть пустым
    @Size(min = 1, max = 10, message = "error.valid.file_ext.size_should_min_max")
    //Размер расширение должен быть в диапазоне от минимального до максимального
    @Pattern(regexp = "^\\w+$", message = "error.valid.file_ext.unacceptable.format")
    //Расширение имеет неприемлемый формат
    @Schema(description = "Расширение")
    String fileExt = "";
}
