package com.example.tutor.model.tutorDetails;

import com.example.tutor.db.entity.TutorReview;
import com.example.tutor.model.BasePageResponse;
import com.example.tutor.util.FileUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Schema(description = "Ответ с пагинацией для списка отзывов о тьюторах")
public class PageTutorReviewResponseDto extends BasePageResponse {

    @Schema(description = "Контент (список отзывов)", implementation = TutorReviewResponseDto.class)
    private List<TutorReviewResponseDto> content;

    @Schema(description = "Текущая страница")
    private int currentPage;

    public static PageTutorReviewResponseDto from(Page<TutorReview> rootPage, List<TutorReview> allReviews, FileUtils fileUtils) {
        List<TutorReviewResponseDto> rootResponses = rootPage.getContent().stream()
                .filter(r -> r.getParent() == null)
                .map(r -> TutorReviewResponseDto.from(r, fileUtils, allReviews))
                .collect(Collectors.toList());

        return PageTutorReviewResponseDto.builder()
                .totalPages(rootPage.getTotalPages())
                .totalElements(rootPage.getTotalElements())
                .currentPage(rootPage.getNumber())
                .size(rootPage.getSize())
                .content(rootResponses)
                .build();
    }
}
