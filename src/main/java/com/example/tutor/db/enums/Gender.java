package com.example.tutor.db.enums;

import lombok.Getter;

@Getter
public enum Gender {

    MALE("male"),
    FEMALE("female");

    final String gender;

    Gender(String gender) {
        this.gender = gender;
    }
}
