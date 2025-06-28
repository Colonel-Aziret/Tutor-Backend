package com.example.tutor.exception;

public class EntityExistsException extends RuntimeException {
    public EntityExistsException(String e) {
        super(e);
    }
}
