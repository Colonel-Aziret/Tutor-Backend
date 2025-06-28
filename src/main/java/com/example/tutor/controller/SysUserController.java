package com.example.tutor.controller;


import com.example.tutor.model.BaseResponse;
import com.example.tutor.model.user.PhotoResponseDto;
import com.example.tutor.model.user.filter.UserFilterDto;
import com.example.tutor.model.user.request.*;
import com.example.tutor.model.user.response.PageSysUserDtoResponse;
import com.example.tutor.model.user.response.SysUserResponseDto;
import com.example.tutor.service.SysUserService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Создание, обновление, удаление и получение")
public class SysUserController {

    private final SysUserService service;


    @GetMapping("/{id}")
    @Operation(
            summary = "Получить пользователя по ID",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SysUserResponseDto.class)))
    )
    ResponseEntity<BaseResponse> findById(@Parameter(description = "ID Пользователя", required = true) @PathVariable Long id) {
        SysUserResponseDto responseDto = service.getById(id);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(responseDto)
                        .build(), HttpStatus.OK
        );
    }

    @PostMapping(value = "/upload-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Загрузить фото пользователя",
            parameters = {
                    @Parameter(
                            required = true,
                            description = "JWT токен",
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            schema = @Schema(type = "string", format = "jwt")
                    )
            }
    )
    public ResponseEntity<BaseResponse> uploadUserPhoto(
            @Parameter(
                    description = "Фото пользователя",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) {
        PhotoResponseDto responseDto = service.uploadPhoto(file, request);

        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(responseDto)
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/get-photo")
    @Operation(summary = "Получить фото пользователя")
    public ResponseEntity<BaseResponse> getUserPhoto(@RequestParam("userId") Long userId) {
        PhotoResponseDto responseDto = service.getPhoto(userId);

        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(responseDto)
                        .build(),
                HttpStatus.OK
        );
    }


    @PutMapping
    @Operation(summary = "Обновление пользователя",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SysUserResponseDto.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO для обновления данных пользователя",
                    content = @Content(schema = @Schema(implementation = SysUserRequest.class))
            ),
            parameters = @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt")))
    ResponseEntity<BaseResponse> update(@Validated @RequestBody SysUserRequest userDto, HttpServletRequest request) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.update(userDto, request))
                        .build(), HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить пользователя по ID",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = Long.class))),
            parameters = @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt")))
    ResponseEntity<BaseResponse> delete(@Parameter(description = "ID пользователя", required = true) @PathVariable Long id, HttpServletRequest request) {
        service.delete(id, request);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(id)
                        .build(), HttpStatus.OK
        );
    }


    @GetMapping("/by-jwt")
    @Operation(
            summary = "Получение данных пользователя по JWT",
            responses = @ApiResponse(
                    description = "Успешный ответ с данными пользователя",
                    content = @Content(schema = @Schema(implementation = SysUserResponseDto.class))
            ),
            parameters = @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt")
            )
    )
    ResponseEntity<BaseResponse> getByJWT(HttpServletRequest request) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.getByJWT(request))
                        .build(), HttpStatus.OK
        );
    }

    @PostMapping("/get-all")
    @Operation(summary = "Получить список доступных пользователей",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = PageSysUserDtoResponse.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO для фильтрации - необязательное",
                    content = @Content(schema = @Schema(implementation = UserFilterDto.class))
            ))
    public ResponseEntity<BaseResponse> findActiveUsers(
            @Parameter(description = "DTO для фильтрации - необязательное") @RequestBody(required = false) UserFilterDto filter) {

        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(service.findAll(filter))
                        .build(), HttpStatus.OK
        );
    }
}




