package com.example.tutor.constraint;

import com.example.tutor.constraint.validator.FutureDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FutureDateValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureDate {

    String message() default "error.valid.user.temporary_access_until_time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
