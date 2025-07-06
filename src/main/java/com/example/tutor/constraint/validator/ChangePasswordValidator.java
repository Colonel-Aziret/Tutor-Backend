package com.example.tutor.constraint.validator;

import com.example.tutor.db.entity.sys.SysUser;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.example.tutor.constraint.ChangePassword;
import com.example.tutor.db.entity.sys.SysRole;
import com.example.tutor.db.repository.SysUserRepository;
import com.example.tutor.model.user.request.SysUserChangePasswordRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class ChangePasswordValidator implements ConstraintValidator<ChangePassword, SysUserChangePasswordRequestDto> {

    private final SysUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public void initialize(ChangePassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(SysUserChangePasswordRequestDto value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        boolean isValid = true;

        SysUser user = null;

        // Если указан email — валидируем и ищем по нему
        if (value.getEmail() != null && !value.getEmail().isBlank()) {
            if (!EMAIL_PATTERN.matcher(value.getEmail()).matches()) {
                context.buildConstraintViolationWithTemplate("error.valid.email.format")
                        .addPropertyNode("email")
                        .addConstraintViolation();
                return false;
            }

            var optionalUser = repository.findByEmail(value.getEmail());
            if (optionalUser.isEmpty()) {
                context.buildConstraintViolationWithTemplate("error.user.not_found.email")
                        .addPropertyNode("email")
                        .addConstraintViolation();
                return false;
            }

            user = optionalUser.get();

            // Проверка: email текущего пользователя
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!authentication.getName().equalsIgnoreCase(value.getEmail())) {
                context.buildConstraintViolationWithTemplate("error.valid.password_change.not_own")
                        .addPropertyNode("email")
                        .addConstraintViolation();
                return false;
            }

        } else {
            // Берём текущего пользователя из контекста
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentEmail = authentication.getName();

            var optionalUser = repository.findByEmail(currentEmail);
            if (optionalUser.isEmpty()) {
                context.buildConstraintViolationWithTemplate("error.user.not_found.email")
                        .addConstraintViolation();
                return false;
            }

            user = optionalUser.get();
        }

        // Валидация нового пароля
        if (value.getPassword() == null || value.getPassword().isBlank()) {
            context.buildConstraintViolationWithTemplate("error.valid.password")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            return false;
        }

        var passwordLength = user.getRoles().stream()
                .mapToInt(SysRole::getPasswordLength)
                .max().orElse(8);

        String dynamicPattern = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{" + passwordLength + ",}$";

        if (!value.getPassword().matches(dynamicPattern)) {
            context.buildConstraintViolationWithTemplate("error.valid.password")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            return false;
        }

        // Старый пароль
        if (value.getOldPassword() == null || value.getOldPassword().isBlank()) {
            context.buildConstraintViolationWithTemplate("error.valid.old_password")
                    .addPropertyNode("oldPassword")
                    .addConstraintViolation();
            isValid = false;
        } else if (!passwordEncoder.matches(value.getOldPassword(), user.getPassword())) {
            context.buildConstraintViolationWithTemplate("error.valid.old_password.incorrect")
                    .addPropertyNode("oldPassword")
                    .addConstraintViolation();
            isValid = false;
        }

        // Повтор пароля
        if (passwordEncoder.matches(value.getPassword(), user.getPassword()) ||
                (user.getLastPassword() != null && passwordEncoder.matches(value.getPassword(), user.getLastPassword())) ||
                (user.getSecondLastPassword() != null && passwordEncoder.matches(value.getPassword(), user.getSecondLastPassword())) ||
                (user.getThirdLastPassword() != null && passwordEncoder.matches(value.getPassword(), user.getThirdLastPassword()))
        ) {
            context.buildConstraintViolationWithTemplate("error.valid.new_password.same_as_old")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
