package com.example.tutor.controller;

import com.example.tutor.model.BaseResponse;
import com.example.tutor.model.actionLog.filter.ActionLogFilter;
import com.example.tutor.model.actionLog.response.PageActionLogResponse;
import com.example.tutor.service.ActionLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/action-log")
@RequiredArgsConstructor
@Tag(name = "Журнал действий", description = "Получить журнал")
public class ActionLogController {

    private final ActionLogService service;

    @PostMapping("/get-all")
    @Operation(summary = "Получить список всех записей с фильтрацией",
            responses = @ApiResponse(
                    description = "Список всех записей",
                    content = @Content(schema = @Schema(implementation = PageActionLogResponse.class))
            ),
            parameters = @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt")))
    public ResponseEntity<BaseResponse> findAll(
            @Parameter(description = "DTO для фильтрации")
            @RequestBody ActionLogFilter filter) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.getAll(filter))
                        .build(), HttpStatus.OK
        );
    }

}
