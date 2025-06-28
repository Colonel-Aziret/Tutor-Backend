package com.example.tutor.model.role.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.example.tutor.db.entity.sys.SysRole;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@Schema(description = "DTO для ответа роли")
public class SysRoleResponseDto {
    @Schema(description = "Идентификатор роли", example = "1")
    Long id;

    @Schema(description = "Алиас", example = "administrator")
    String alias;

    @Schema(description = "Наименование на русском", example = "Администратор")
    String nameRu;

    @Schema(description = "Наименование на кыргызском", example = "Администратор")
    String nameKy;

    @Schema(description = "Длина пароля для этой роли")
    Integer passwordLength;

    @Schema(description = "Удален", example = "false")
    Boolean deleted;

    @Schema(description = "Время редактирования", example = "2023-10-25T14:32:00Z")
    Date editedTime;

    @Schema(description = "Время создания записи", example = "2023-10-25T14:32:00Z")
    Date createdTime;

    public static SysRoleResponseDto from(SysRole role) {
        return SysRoleResponseDto.builder()
                .id(role.getId())
                .alias(role.getAlias())
                .nameRu(role.getNameRu())
                .passwordLength(role.getPasswordLength())
                .nameKy(role.getNameKy())
                .deleted(role.isDeleted())
                .editedTime(role.getEditedTime())
                .createdTime(role.getCreatedTime())
                .build();
    }
}
