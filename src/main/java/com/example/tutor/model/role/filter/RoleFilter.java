package com.example.tutor.model.role.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import com.example.tutor.model.BasePageRequest;
import lombok.Data;

@Data
@Schema(description = "Модель для фильтрации")
public class RoleFilter extends BasePageRequest {

    @Schema(description = "Доступные/Удаленные роли", example = "false")
    Boolean deleted = false;

    @Schema(description = "Регистрация/Добавление", example = "true")
    Boolean registration = false;

}
