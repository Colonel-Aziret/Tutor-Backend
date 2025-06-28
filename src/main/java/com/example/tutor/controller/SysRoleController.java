package com.example.tutor.controller;

import com.example.tutor.model.BaseResponse;
import com.example.tutor.model.role.filter.RoleFilter;
import com.example.tutor.model.role.request.SysRoleRequest;
import com.example.tutor.model.role.response.PageSysRoleResponse;
import com.example.tutor.model.role.response.SysRoleResponseDto;
import com.example.tutor.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@Tag(name = "Роли", description = "Создание, обновление, удаление и получение")
public class SysRoleController {

    private final SysRoleService service;

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить роль по ID",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SysRoleResponseDto.class))),
            parameters = @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt"))
    )
    ResponseEntity<BaseResponse> findById(@Parameter(description = "ID Роли", required = true) @PathVariable Long id) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(SysRoleResponseDto.from(service.findById(id)))
                        .build(), HttpStatus.OK
        );
    }

    @PostMapping("/get-all")
    @Operation(
            summary = "Получить список текущих доступных ролей",
            responses = @ApiResponse(
                    description = "Список активных ролей",
                    content = @Content(schema = @Schema(implementation = PageSysRoleResponse.class)
                    )),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO для фильтрации",
                    content = @Content(schema = @Schema(implementation = RoleFilter.class))
            )
    )
    ResponseEntity<BaseResponse> getAll(
            @RequestBody RoleFilter filter
    ) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.getAll(filter))
                        .build(), HttpStatus.OK
        );
    }


    @PostMapping()
    @Operation(summary = "Создать роль",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SysRoleResponseDto.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO для создания новой роли",
                    content = @Content(schema = @Schema(implementation = SysRoleRequest.class))
            ),
            parameters = @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt")))
    ResponseEntity<BaseResponse> create(@Validated @RequestBody SysRoleRequest roleCreateDto, HttpServletRequest request) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.create(roleCreateDto, request))
                        .build(), HttpStatus.OK
        );
    }

    @PutMapping
    @Operation(summary = "Обновить роль",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SysRoleResponseDto.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO для обновления ролей",
                    content = @Content(schema = @Schema(implementation = SysRoleRequest.class))
            ),
            parameters = @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt"))
    )
    ResponseEntity<BaseResponse> update(@RequestBody SysRoleRequest requestDto, HttpServletRequest request) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.update(requestDto, request))
                        .build(), HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление роли", responses = @ApiResponse(content = @Content(schema =
    @Schema(implementation = Long.class))),
            parameters = @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt")))
    ResponseEntity<BaseResponse> delete(@Parameter(required = true, description = "Удаление роли по ID") @PathVariable Long id, HttpServletRequest request) {
        service.delete(id, request);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(id)
                        .build(), HttpStatus.OK
        );
    }
}
