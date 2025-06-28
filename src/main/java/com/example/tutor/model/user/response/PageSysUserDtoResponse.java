package com.example.tutor.model.user.response;

import com.example.tutor.db.entity.SubjectExperience;
import com.example.tutor.db.entity.sys.TutorDetails;
import com.example.tutor.db.entity.sys.TutorEducation;
import com.example.tutor.util.FileUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import com.example.tutor.db.entity.sys.SysUser;
import com.example.tutor.model.BasePageResponse;
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
@Schema(description = "Ответ с пагинацией для списка пользователей")
public class PageSysUserDtoResponse extends BasePageResponse {
    @Schema(description = "Контент (список пользователей)", implementation = SysUserResponseDto.class)
    private List<SysUserResponseDto> content;

    public static PageSysUserDtoResponse from(Page<SysUser> page, FileUtils fileUtils) {
        return PageSysUserDtoResponse.builder()
                .content(page.getContent().stream()
                        .map(user -> {
                            TutorDetails details = user.getTutorDetails();
                            List<TutorEducation> educations = details != null ? details.getEducations() : Collections.emptyList();
                            List<SubjectExperience> experiences = details != null ? details.getSubjectExperiences() : Collections.emptyList();

                            return SysUserResponseDto.fromForTutor(user, details, educations, experiences, fileUtils);
                        })
                        .collect(Collectors.toList()))
                .page(page.getNumber() + 1)
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}

