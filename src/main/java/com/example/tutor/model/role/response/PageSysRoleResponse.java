package com.example.tutor.model.role.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.example.tutor.db.entity.sys.SysRole;
import com.example.tutor.model.BasePageResponse;
import com.example.tutor.model.user.response.SysUserResponseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Schema(description = "Ответ с пагинацией для списка ролей")
public class PageSysRoleResponse extends BasePageResponse {
    @Schema(description = "Контент (список ролей)", implementation = SysUserResponseDto.class)
    private List<SysRoleResponseDto> content;

    public static PageSysRoleResponse from(Page<SysRole> page) {
        return PageSysRoleResponse.builder()
                .content(page.getContent().stream()
                        .map(SysRoleResponseDto::from)
                        .collect(Collectors.toList()))
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

}
