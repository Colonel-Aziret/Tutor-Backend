package com.example.tutor.constraint.validator;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.example.tutor.constraint.ValidUser;
import com.example.tutor.db.entity.sys.SysRole;
import com.example.tutor.db.entity.sys.SysUser;
import com.example.tutor.db.repository.SysRoleRepository;
import com.example.tutor.db.repository.SysUserRepository;
import com.example.tutor.model.user.request.SysUserRequest;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.regex.Pattern;


@RequiredArgsConstructor
public class UserCreateValidator implements ConstraintValidator<ValidUser, SysUserRequest> {

    private final SysUserRepository repository;
    private final SysRoleRepository roleRepository;


    @Override
    public void initialize(ValidUser constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SysUserRequest value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        boolean isValid = true;


        // Проверка телефона
        String phoneNumber = value.getPhone();
        if (phoneNumber == null || phoneNumber.isBlank()) {
            context.buildConstraintViolationWithTemplate("error.valid.phoneNumber.is_blank")
                    .addPropertyNode("phone")
                    .addConstraintViolation();
            return false;
        }

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

        try {
            Phonenumber.PhoneNumber parsed = phoneUtil.parse(phoneNumber, null);

            if (!phoneUtil.isValidNumber(parsed)) {
                context.buildConstraintViolationWithTemplate("error.phone.invalid_number")
                        .addPropertyNode("phone")
                        .addConstraintViolation();
                return false;
            }

            String normalizedPhone = phoneUtil.format(parsed, PhoneNumberUtil.PhoneNumberFormat.E164);
            value.setPhone(normalizedPhone);

            // 💡 Загружаем текущего пользователя по ID, если редактирование
            Optional<SysUser> currentUserOpt = Optional.ofNullable(value.getId())
                    .flatMap(repository::findById);

            Optional<SysUser> userByPhone = repository.findByPhoneNumber(normalizedPhone);

            if (userByPhone.isPresent()) {
                // Если это другой пользователь — ошибка
                if (currentUserOpt.isEmpty() || !userByPhone.get().getId().equals(currentUserOpt.get().getId())) {
                    context.buildConstraintViolationWithTemplate("error.valid.user.phone.exists")
                            .addPropertyNode("phone")
                            .addConstraintViolation();
                    return false;
                }
            }

        } catch (NumberParseException e) {
            context.buildConstraintViolationWithTemplate("error.phone.invalid_format")
                    .addPropertyNode("phone")
                    .addConstraintViolation();
            return false;
        }

        // Проверка email
        String email = value.getEmail();
        String emailPattern = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";

        if (email == null || email.isBlank()) {
            context.buildConstraintViolationWithTemplate("error.valid.email.not_null")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            return false;
        }

        if (!Pattern.matches(emailPattern, email)) {
            context.buildConstraintViolationWithTemplate("error.valid.email.format")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            return false;
        }

        Optional<SysUser> userByEmail = repository.findByEmail(email);
        if (userByEmail.isPresent() && (value.getId() == null || !userByEmail.get().getId().equals(value.getId()))) {
            context.buildConstraintViolationWithTemplate("error.valid.user.email")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            return false;
        }

        boolean isUpdate = value.getId() != null;

        String pattern = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$";

        if (!isUpdate) {
            if (value.getPassword() == null || value.getPassword().isBlank()) {
                context.buildConstraintViolationWithTemplate("error.valid.password")
                        .addPropertyNode("password")
                        .addConstraintViolation();
                return false;
            }

            if (!value.getPassword().matches(pattern)) {
                context.buildConstraintViolationWithTemplate("error.valid.password.format")
                        .addPropertyNode("password")
                        .addConstraintViolation();
                return false;
            }

        } else {
            if (value.getPassword() != null && !value.getPassword().isBlank()) {
                if (!value.getPassword().matches(pattern)) {
                    context.buildConstraintViolationWithTemplate("error.valid.password.format")
                            .addPropertyNode("password")
                            .addConstraintViolation();
                    return false;
                }
            }
        }

        return isValid;
    }
}
