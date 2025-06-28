package com.example.tutor.controller;

import com.example.tutor.model.BaseResponse;
import com.example.tutor.model.user.request.ForgotPasswordRequestDto;
import com.example.tutor.model.user.request.ResetPasswordRequestDto;
import com.example.tutor.model.user.request.SysUserChangePasswordRequestDto;
import com.example.tutor.model.user.request.SysUserRequest;
import com.example.tutor.model.user.response.SysUserResponseDto;
import com.example.tutor.query_limiter.QueryLimited;
import com.example.tutor.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(
        name = "Аутентификация и управление доступом",
        description = "Регистрация, вход, выход, активация, восстановление и смена пароля"
)
public class AuthController {
    private final SysUserService service;

    @PostMapping("/registration")
    @Operation(summary = "Создание нового пользователя",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SysUserResponseDto.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO для создания нового пользователя",
                    content = @Content(schema = @Schema(implementation = SysUserRequest.class))
            ))
    ResponseEntity<BaseResponse> create(@Validated @RequestBody SysUserRequest userDto, HttpServletRequest request) throws Exception {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.create(userDto, request))
                        .build(), HttpStatus.OK
        );
    }

    @QueryLimited
    @PostMapping("/activate")
    @Operation(summary = "Активация пользователя",
            responses = @ApiResponse(content = @Content(schema =
            @Schema(implementation = SysUserResponseDto.class))))
    ResponseEntity<BaseResponse> activateUser(@RequestParam(name = "activationCode") UUID activationCode) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg("The user was successfully activated")
                        .res(service.activateUser(activationCode))
                        .build(), HttpStatus.OK
        );

    }


    @PostMapping("/forgot-password")
    @Operation(summary = "Запрос на восстановление пароля")
    public ResponseEntity<BaseResponse> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDto requestDto) throws MessagingException {
        service.processForgotPassword(requestDto.getEmail());
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(null)
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/reset-password/{token}")
    @Operation(summary = "Проверка токена сброса пароля")
    public ResponseEntity<BaseResponse> checkToken(@PathVariable String token) {
        service.validateToken(token);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(null)
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Сброс пароля")
    public ResponseEntity<BaseResponse> resetPassword(@RequestBody @Valid ResetPasswordRequestDto requestDto) {
        service.resetPassword(requestDto);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(null)
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping("/change-password")
    @Operation(summary = "Обновление пароля пользователя",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SysUserResponseDto.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO для смены пароля пользователя",
                    content = @Content(schema = @Schema(implementation = SysUserChangePasswordRequestDto.class))
            ),
            parameters =
            @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt"))
    )
    ResponseEntity<BaseResponse> changeUserPassword(@Validated @RequestBody SysUserChangePasswordRequestDto userDto, HttpServletRequest request) {

        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.changePassword(userDto, request))
                        .build(), HttpStatus.OK
        );
    }

    @PostMapping("/change-password-by-user")
    @Operation(summary = "Обновление пароля текущего пользователя",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SysUserResponseDto.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO для смены пароля пользователя",
                    content = @Content(schema = @Schema(implementation = SysUserChangePasswordRequestDto.class))
            ),
            parameters =
            @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt"))
    )
    ResponseEntity<BaseResponse> changeCurrentUserPassword(@Validated @RequestBody SysUserChangePasswordRequestDto userDto, HttpServletRequest request) {

        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.changePasswordCurrentUser(userDto, request))
                        .build(), HttpStatus.OK
        );
    }

}
