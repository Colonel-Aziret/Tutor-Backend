package com.example.tutor.constraint;

import com.example.tutor.constraint.validator.AdminChangePasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AdminChangePasswordValidator.class)
public @interface AdminChangePassword {

    String message() default "Invalid input data";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
