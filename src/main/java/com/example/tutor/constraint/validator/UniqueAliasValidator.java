package com.example.tutor.constraint.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.example.tutor.constraint.UniqueAlias;
import com.example.tutor.db.repository.AliasRepository;
import com.example.tutor.util.ApplicationContextProvider;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.util.Optional;

@RequiredArgsConstructor
public class UniqueAliasValidator implements ConstraintValidator<UniqueAlias, Object> {

    private AliasRepository<?> repository;
    private String aliasField;

    @Override
    public void initialize(UniqueAlias constraintAnnotation) {
        this.repository = (AliasRepository<?>) ApplicationContextProvider
                .getApplicationContext()
                .getBean(constraintAnnotation.repository());
        this.aliasField = constraintAnnotation.aliasField();
    }


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            context.disableDefaultConstraintViolation();

            Field aliasField = value.getClass().getDeclaredField(this.aliasField);
            aliasField.setAccessible(true);
            String alias = (String) aliasField.get(value);

            Field idField = null;
            Long id = null;

            try {
                idField = value.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                id = (Long) idField.get(value);
            } catch (NoSuchFieldException ignored) {

            }

            if (alias == null || alias.isBlank()) {
                context.buildConstraintViolationWithTemplate("error.valid.alias.not_null")
                        .addPropertyNode(this.aliasField)
                        .addConstraintViolation();
                return false;
            }

            Optional<?> existingEntity = repository.findByAlias(alias);

            if (existingEntity.isPresent()) {
                Object entity = existingEntity.get();

                Field deletedField = entity.getClass().getDeclaredField("deleted");
                deletedField.setAccessible(true);
                Boolean isDeleted = (Boolean) deletedField.get(entity);

                if (id != null) {
                    Field entityIdField = entity.getClass().getDeclaredField("id");
                    entityIdField.setAccessible(true);
                    Long entityId = (Long) entityIdField.get(entity);

                    if (isDeleted != null && isDeleted && !id.equals(entityId)) {
                        context.buildConstraintViolationWithTemplate("error.valid.alias.not_unique")
                                .addPropertyNode(this.aliasField)
                                .addConstraintViolation();
                        return false;
                    }

                    if (id.equals(entityId)) {
                        return true;
                    }
                }


                if (isDeleted != null && isDeleted) {
                    return true;
                }


                context.buildConstraintViolationWithTemplate("error.valid.alias.not_unique")
                        .addPropertyNode(this.aliasField)
                        .addConstraintViolation();
                return false;
            }

            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Ошибка при проверке уникальности alias", e);
        }
    }
}
