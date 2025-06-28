package com.example.tutor.model.tutorDetails;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class SubjectExperienceRequestDto {
    Long subjectId;

    @Schema(description = "Список описаний опыта по предмету", example = "[\"1 год преподавания в школе\", \"2 года репетиторства\"]")
    List<String> experienceDescriptions;
}
