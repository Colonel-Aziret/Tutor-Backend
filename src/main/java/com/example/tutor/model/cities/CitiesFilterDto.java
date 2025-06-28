package com.example.tutor.model.cities;

import com.example.tutor.model.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Модель для фильтрации городов")
@Data
public class CitiesFilterDto extends BasePageRequest {
    @Schema(description = "Фильтрация по алиасу", example = "ru")
    String alias;

    @Schema(description = "Фильтрация по статусу удаления", example = "false")
    Boolean deleted = false;

    @Schema(description = "Название города (рус/кырг) для поиска", example = "Бишкек")
    String name;
}
