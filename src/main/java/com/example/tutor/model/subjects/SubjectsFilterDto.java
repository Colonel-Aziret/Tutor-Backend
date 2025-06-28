package com.example.tutor.model.subjects;

import com.example.tutor.model.BasePageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Модель для фильтрации предметов")
@Data
public class SubjectsFilterDto extends BasePageRequest {

    @Schema(description = "Фильтрация по алиасу", example = "ru")
    String alias;

    @Schema(description = "Фильтрация по статусу удаления", example = "false")
    Boolean deleted = false;

    @Schema(description = "Название города (рус/кырг) для поиска", example = "Бишкек")
    String name;

}
