package com.example.tutor.controller;

import com.example.tutor.model.BaseResponse;
import com.example.tutor.model.tutorDetails.PageTutorReviewResponseDto;
import com.example.tutor.model.tutorDetails.TutorReviewFilterDto;
import com.example.tutor.model.tutorDetails.TutorReviewRequestDto;
import com.example.tutor.service.ReviewService;
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
@RequiredArgsConstructor
@RequestMapping("/reviews")
@Tag(name = "Отзывы", description = "Создание, обновление, удаление и получение")
public class ReviewController {
    private final ReviewService service;

    @PostMapping("/comments")
    @Operation(
            summary = "Получить отзывы о тьюторах с пагинацией",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = false,
                    description = "Фильтр для отзывов о тьюторах",
                    content = @Content(schema = @Schema(implementation = TutorReviewFilterDto.class))
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PageTutorReviewResponseDto.class))
            )
    )
    public ResponseEntity<BaseResponse> getReviews(
            @RequestBody(required = false) TutorReviewFilterDto filterDto
    ) {
        PageTutorReviewResponseDto response = service.getAllComments(filterDto);

        return ResponseEntity.ok(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(response)
                        .build()
        );
    }



    @PostMapping("/comment")
    @Operation(summary = "Оставить отзыв о тьюторе",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "DTO для отзыва о тьюторе",
                    content = @Content(schema = @Schema(implementation = TutorReviewRequestDto.class))
            ),
            parameters = @Parameter(
                    required = true,
                    description = "JWT токен",
                    in = ParameterIn.HEADER,
                    name = "Authorization",
                    schema = @Schema(type = "string", format = "jwt"))
    )
    public ResponseEntity<BaseResponse> addTutorReview(@Validated @RequestBody TutorReviewRequestDto requestDto,
                                                       HttpServletRequest request) {
        service.addReview(requestDto);

        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg(null)
                        .res(null)
                        .build(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete-comment/{id}")
    @Operation(summary = "Удалить свой отзыв о тьюторе",
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
    public ResponseEntity<BaseResponse> deleteTutorReview(@Parameter(description = "ID комментария", required = true) @PathVariable Long id, HttpServletRequest request) {
        service.deleteReview(id);

        return new ResponseEntity<>(
                BaseResponse.builder()
                        .success(BaseController.Constants.SUCCESS)
                        .msg("Отзыв успешно удалён")
                        .res(id)
                        .build(),
                HttpStatus.OK
        );
    }

}
