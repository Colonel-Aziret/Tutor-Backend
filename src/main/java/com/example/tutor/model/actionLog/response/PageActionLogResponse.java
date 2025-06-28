package com.example.tutor.model.actionLog.response;

import com.example.tutor.db.entity.ActionLog;
import com.example.tutor.model.BasePageResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
@Schema(description = "Ответ страницы с журналом действий")
public class PageActionLogResponse extends BasePageResponse {

    @ArraySchema(schema =
    @Schema(description = "Журнал", implementation = ActionLogResponse.class))
    List<ActionLogResponse> content;

    public static PageActionLogResponse from(Page<ActionLog> page) {
        return PageActionLogResponse.builder()
                .content(page.getContent().stream()
                        .map(ActionLogResponse::from)
                        .collect(Collectors.toList()))
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .build();
    }

}
