package com.example.tutor.constraint.validator;

import com.example.tutor.constraint.AdminChangePassword;
import com.example.tutor.db.entity.sys.SysRole;
import com.example.tutor.db.entity.sys.SysUser;
import com.example.tutor.db.repository.SysUserRepository;
import com.example.tutor.model.user.request.AdminChangePasswordRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class AdminChangePasswordValidator implements ConstraintValidator<AdminChangePassword,
        AdminChangePasswordRequestDto> {

    private final SysUserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public void initialize(AdminChangePassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(AdminChangePasswordRequestDto value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (value.getEmail() == null || value.getEmail().isBlank()) {
            context.buildConstraintViolationWithTemplate("error.valid.email.not_null")
                    .addPropertyNode("email")
                    .addConstraintViolation();
            return false;
        }

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
        SysUser user = optionalUser.get();


        if (value.getPassword() == null || value.getPassword().isBlank()) {
            context.buildConstraintViolationWithTemplate("error.valid.password")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            return false;
        }

        var passwordLength = user.getRoles().stream()
                .mapToInt(SysRole::getPasswordLength)
                .max().getAsInt();

        String dynamicPattern = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{" + passwordLength + ",}$";

        if (!value.getPassword().matches(dynamicPattern)) {
            context.buildConstraintViolationWithTemplate("error.valid.password")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            return false;
        }

        if (passwordEncoder.matches(value.getPassword(), user.getPassword()) ||
                (user.getLastPassword() != null && passwordEncoder.matches(value.getPassword(),
                        user.getLastPassword())) ||
                (user.getSecondLastPassword() != null && passwordEncoder.matches(value.getPassword(), user.getSecondLastPassword())) ||
                (user.getThirdLastPassword() != null && passwordEncoder.matches(value.getPassword(), user.getThirdLastPassword()))
        ) {
            context.buildConstraintViolationWithTemplate("error.valid.new_password.same_as_old")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
