package com.example.tutor.controller.hb;

import com.example.tutor.controller.BaseController;
import com.example.tutor.model.BaseResponse;

import com.example.tutor.model.subjects.PageSubjectsResponseDto;
import com.example.tutor.model.subjects.SubjectsFilterDto;
import com.example.tutor.model.subjects.SubjectsRequestDto;
import com.example.tutor.model.subjects.SubjectsResponseDto;
import com.example.tutor.service.hb.SubjectsService;
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
@RequestMapping("/hb-subjects")
@RequiredArgsConstructor
@Tag(name = "Предметы", description = "Создание, обновление, удаление и получение")
public class SubjectsController {
 

    private final SubjectsService service;
    
        @GetMapping("/{id}")
        @Operation(
                summary = "Получить предмет по ID",
                responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SubjectsResponseDto.class))),
                parameters = @Parameter(
                        required = true,
                        description = "JWT токен",
                        in = ParameterIn.HEADER,
                        name = "Authorization",
                        schema = @Schema(type = "string", format = "jwt"))
        )
        public ResponseEntity<BaseResponse> findById(
                @Parameter(description = "ID предмета", required = true) @PathVariable Long id) {
            return new ResponseEntity<>(
                    BaseResponse.builder()
                            .success(BaseController.Constants.SUCCESS)
                            .msg(null)
                            .res(SubjectsResponseDto.from(service.findById(id)))
                            .build(),
                    HttpStatus.OK
            );
        }

        @PostMapping("/get-all")
        @Operation(
                summary = "Получить список предметов",
                responses = @ApiResponse(
                        description = "Список предметов",
                        content = @Content(schema = @Schema(implementation = PageSubjectsResponseDto.class)
                        )),
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                        required = true,
                        description = "DTO для фильтрации предметов",
                        content = @Content(schema = @Schema(implementation = SubjectsFilterDto.class))
                )
        )
        public ResponseEntity<BaseResponse> getAll(@RequestBody SubjectsFilterDto filter) {
            return new ResponseEntity<>(
                    BaseResponse.builder()
                            .success(BaseController.Constants.SUCCESS)
                            .msg(null)
                            .res(service.getAll(filter))
                            .build(),
                    HttpStatus.OK
            );
        }

        @PostMapping
        @Operation(
                summary = "Создать предмет",
                responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SubjectsResponseDto.class))),
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                        required = true,
                        description = "DTO для создания предмета",
                        content = @Content(schema = @Schema(implementation = SubjectsRequestDto.class))
                ),
                parameters = @Parameter(
                        required = true,
                        description = "JWT токен",
                        in = ParameterIn.HEADER,
                        name = "Authorization",
                        schema = @Schema(type = "string", format = "jwt"))
        )
        public ResponseEntity<BaseResponse> create(@Validated @RequestBody SubjectsRequestDto dto, HttpServletRequest request) {
            return new ResponseEntity<>(
                    BaseResponse.builder()
                            .success(BaseController.Constants.SUCCESS)
                            .msg(null)
                            .res(service.create(dto, request))
                            .build(),
                    HttpStatus.OK
            );
        }

        @PutMapping
        @Operation(
                summary = "Обновить предмет",
                responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SubjectsResponseDto.class))),
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                        required = true,
                        description = "DTO для обновления предмета",
                        content = @Content(schema = @Schema(implementation = SubjectsRequestDto.class))
                ),
                parameters = @Parameter(
                        required = true,
                        description = "JWT токен",
                        in = ParameterIn.HEADER,
                        name = "Authorization",
                        schema = @Schema(type = "string", format = "jwt"))
        )
        public ResponseEntity<BaseResponse> update(@RequestBody SubjectsRequestDto dto, HttpServletRequest request) {
            return new ResponseEntity<>(
                    BaseResponse.builder()
                            .success(BaseController.Constants.SUCCESS)
                            .msg(null)
                            .res(service.update(dto, request))
                            .build(),
                    HttpStatus.OK
            );
        }

        @DeleteMapping("/{id}")
        @Operation(
                summary = "Удаление предмета",
                responses = @ApiResponse(content = @Content(schema = @Schema(implementation = Long.class))),
                parameters = @Parameter(
                        required = true,
                        description = "JWT токен",
                        in = ParameterIn.HEADER,
                        name = "Authorization",
                        schema = @Schema(type = "string", format = "jwt"))
        )
        public ResponseEntity<BaseResponse> delete(
                @Parameter(required = true, description = "ID предмета") @PathVariable Long id,
                HttpServletRequest request) {
            service.delete(id, request);
            return new ResponseEntity<>(
                    BaseResponse.builder()
                            .success(BaseController.Constants.SUCCESS)
                            .msg(null)
                            .res(id)
                            .build(),
                    HttpStatus.OK
            );
        }
    }
