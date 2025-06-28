package com.example.tutor.model.role.request;

import com.example.tutor.constraint.UniqueAlias;
import com.example.tutor.db.repository.SysRoleRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO для запроса роли")
@UniqueAlias(repository = SysRoleRepository.class)
public class SysRoleRequest {

    @Schema(description = "Идентификатор роли", example = "1")
    Long id;

    @NotBlank(message = "error.valid.alias")
    @Schema(description = "Алиас", example = "administrator")
    String alias;

    @NotBlank(message = "error.valid.name_ru")
    @Schema(description = "Наименование на русском", example = "Администратор")
    String nameRu;

    @NotBlank(message = "error.valid.name_ky")
    @Schema(description = "Наименование на кыргызском", example = "Администратор")
    String nameKy;

    @Schema(description = "Идентификатор справочник организации", example = "2")
    Long governmentId;
}
