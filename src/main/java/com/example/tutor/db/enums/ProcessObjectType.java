package com.example.tutor.db.enums;

import lombok.Getter;

@Getter
public enum ProcessObjectType {
    PLAN("План"),
    TASK("Задача"),
    APPLICATION("Заявка");

    private final String name;

    ProcessObjectType(String name) {
        this.name = name;
    }
}
