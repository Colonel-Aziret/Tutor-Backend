package com.example.tutor.model.subjects;

import com.example.tutor.db.entity.HB.HBSubjects;
import com.example.tutor.model.BasePageResponse;
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
@Schema(description = "Ответ с пагинацией для списка предметов")
public class PageSubjectsResponseDto extends BasePageResponse {


    @Schema(description = "Контент (список предметов)", implementation = SubjectsResponseDto.class)
    private List<SubjectsResponseDto> content;

    public static PageSubjectsResponseDto from(Page<HBSubjects> page) {
        return PageSubjectsResponseDto.builder()
                .content(page.getContent().stream()
                        .map(SubjectsResponseDto::from)
                        .collect(Collectors.toList()))
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
