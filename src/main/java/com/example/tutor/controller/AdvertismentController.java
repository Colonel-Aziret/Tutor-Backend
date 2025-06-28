package com.example.tutor.controller;

import com.example.tutor.model.BaseResponse;
import com.example.tutor.model.advertisment.AdvertismentRequestDto;
import com.example.tutor.model.advertisment.AdvertismentResponseDto;
import com.example.tutor.service.AdvertismentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/advertisment")
@RequiredArgsConstructor
@Tag(name = "Реклама", description = "Создание и получение рекламы с фото")
public class AdvertismentController {

    private final AdvertismentService advertismentService;

    @PostMapping("/create")
    @Operation(summary = "Создать новую рекламу с фото",
            description = "Загрузка описания и списка фотографий для рекламы",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Описание и фото рекламы",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            ),
            parameters = @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt"))
    )
    public ResponseEntity<BaseResponse> create(
            @RequestPart("dto") @Validated AdvertismentRequestDto dto,
            @RequestPart("files") List<MultipartFile> files,
            HttpServletRequest request) {

        AdvertismentResponseDto responseDto = advertismentService.create(dto, files, request);
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .success(true)
                        .msg(null)
                        .res(responseDto)
                        .build()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить рекламу по ID",
            description = "Возвращает рекламу с фото по ID")
    public ResponseEntity<BaseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .success(true)
                        .msg(null)
                        .res(advertismentService.findById(id))
                        .build()
        );
    }

    @GetMapping("/get-all")
    @Operation(summary = "Получить список всей рекламы",
            description = "Возвращает список всех реклам с фото")
    public ResponseEntity<BaseResponse> getAll() {
        return ResponseEntity.ok(
                BaseResponse.builder()
                        .success(true)
                        .msg(null)
                        .res(advertismentService.getAll())
                        .build()
        );
    }
}
