package com.example.tutor.controller.hb;

import com.example.tutor.controller.BaseController;
import com.example.tutor.model.BaseResponse;
import com.example.tutor.model.cities.CitiesFilterDto;
import com.example.tutor.model.cities.CitiesRequestDto;
import com.example.tutor.model.cities.CitiesResponseDto;
import com.example.tutor.model.cities.PageCitiesResponseDto;
import com.example.tutor.service.hb.CitiesService;
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
@RequestMapping("/hb-cities")
@RequiredArgsConstructor
@Tag(name = "Города", description = "Создание, обновление, удаление и получение")
public class CitiesController {

    private final CitiesService service;

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить город по ID",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = CitiesResponseDto.class))),
            parameters = @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt"))
    )
    public ResponseEntity<BaseResponse> findById(
            @Parameter(description = "ID города", required = true) @PathVariable Long id) {
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(CitiesResponseDto.from(service.findById(id)))
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping("/get-all")
    @Operation(
            summary = "Получить список городов",
            responses = @ApiResponse(
                    description = "Список городов",
                    content = @Content(schema = @Schema(implementation = PageCitiesResponseDto.class)
                    )),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO для фильтрации городов",
                    content = @Content(schema = @Schema(implementation = CitiesFilterDto.class))
            )
    )
    public ResponseEntity<BaseResponse> getAll(@RequestBody CitiesFilterDto filter) {
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
            summary = "Создать город",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = CitiesResponseDto.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO для создания города",
                    content = @Content(schema = @Schema(implementation = CitiesRequestDto.class))
            ),
            parameters = @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt"))
    )
    public ResponseEntity<BaseResponse> create(@Validated @RequestBody CitiesRequestDto dto, HttpServletRequest request) {
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
            summary = "Обновить город",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = CitiesResponseDto.class))),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO для обновления города",
                    content = @Content(schema = @Schema(implementation = CitiesRequestDto.class))
            ),
            parameters = @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt"))
    )
    public ResponseEntity<BaseResponse> update(@RequestBody CitiesRequestDto dto, HttpServletRequest request) {
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
            summary = "Удаление города",
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = Long.class))),
            parameters = @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt"))
    )
    public ResponseEntity<BaseResponse> delete(
            @Parameter(required = true, description = "ID города") @PathVariable Long id,
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
