package com.example.tutor.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String s) {
        super(s);
    }

    //Использовать для валидации внутри сервиса
}
