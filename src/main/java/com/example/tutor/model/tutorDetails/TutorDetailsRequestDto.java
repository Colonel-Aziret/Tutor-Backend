package com.example.tutor.model.tutorDetails;

import lombok.Data;

@Data
public class TutorDetailsRequestDto {
    Double price;
    Long cityId;
    String aboutMe;
    String telegram;
    Integer experience;
    Boolean online;
    Boolean offline;
    Boolean atTutor;
    String address;
}
