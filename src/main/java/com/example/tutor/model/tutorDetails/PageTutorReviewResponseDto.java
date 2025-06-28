package com.example.tutor.model.tutorDetails;

import com.example.tutor.db.entity.TutorReview;
import com.example.tutor.model.BasePageResponse;
import com.example.tutor.util.FileUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Schema(description = "Ответ с пагинацией для списка отзывов о тьюторах")
public class PageTutorReviewResponseDto extends BasePageResponse {

    @Schema(description = "Контент (список отзывов)", implementation = TutorReviewResponseDto.class)
    private List<TutorReviewResponseDto> content;

    public static PageTutorReviewResponseDto from(Page<TutorReview> page, FileUtils fileUtils) {
        return PageTutorReviewResponseDto.builder()
                .content(page.getContent().stream()
                        .map(review -> TutorReviewResponseDto.from(review, fileUtils))
                        .collect(Collectors.toList()))
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
