package com.example.tutor.constraint;

import com.example.tutor.constraint.validator.UniqueAliasValidator;
import com.example.tutor.db.repository.AliasRepository;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueAliasValidator.class)
public @interface UniqueAlias {

    String message() default "error.valid.alias.not_unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends AliasRepository<?>> repository();

    String aliasField() default "alias";
}
