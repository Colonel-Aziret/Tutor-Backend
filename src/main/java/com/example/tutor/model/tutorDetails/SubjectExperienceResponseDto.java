package com.example.tutor.model.tutorDetails;

import com.example.tutor.db.entity.SubjectExperience;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "Предмет и список опыта преподавания по нему")
public class SubjectExperienceResponseDto {

    @Schema(description = "ID предмета", example = "5")
    Long subjectId;

    @Schema(description = "Название предмета", example = "Химия")
    String subjectName;

    @Schema(description = "Список описаний опыта по предмету", example = "[\"1 год преподавания в школе\", \"2 года репетиторства\"]")
    List<String> experienceDescriptions;
}
