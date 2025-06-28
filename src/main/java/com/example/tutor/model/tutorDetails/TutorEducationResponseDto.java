package com.example.tutor.model.tutorDetails;

import com.example.tutor.db.entity.sys.TutorEducation;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TutorEducationResponseDto {
    String university;
    Integer graduationYear;

    public static TutorEducationResponseDto from(TutorEducation edu) {
        return TutorEducationResponseDto.builder()
                .university(edu.getUniversity())
                .graduationYear(edu.getGraduationYear())
                .build();
    }
}
