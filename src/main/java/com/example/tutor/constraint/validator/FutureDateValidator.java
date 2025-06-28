package com.example.tutor.constraint.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.example.tutor.constraint.FutureDate;

import java.util.Calendar;
import java.util.Date;

public class FutureDateValidator implements ConstraintValidator<FutureDate, Date> {

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Если дата null, пропускаем проверку (валидация других аннотаций выполнится)
        }

        // Получаем текущую дату
        Calendar currentDate = Calendar.getInstance();
        // Добавляем 1 день к текущей дате
        currentDate.add(Calendar.DAY_OF_YEAR, 1);

        // Сравниваем дату, чтобы она была не раньше чем currentDate
        return value.after(currentDate.getTime());
    }
}
