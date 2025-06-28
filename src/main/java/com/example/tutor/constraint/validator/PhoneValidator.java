package com.example.tutor.constraint.validator;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.example.tutor.constraint.ValidServicePhoneNumbers;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PhoneValidator implements ConstraintValidator<ValidServicePhoneNumbers, Object> {

    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        boolean valid = true;

//        if (dto instanceof ServiceRegisterRequestDto serviceDto) {
//            valid = isValid(context, true, serviceDto.getCountryCode(), serviceDto.getPhone(), serviceDto.getSecondPhone(), serviceDto.getSecondCountryCode());
//
//        } else if (dto instanceof ProductRegisterRequestDto productDto) {
//            valid = isValid(context, true, productDto.getCountryCode(), productDto.getPhone(), productDto.getSecondPhone(), productDto.getSecondCountryCode());
//
//        } else {
//            log.warn("PhoneValidator: unsupported class {}", dto.getClass().getName());
//            return true;
//        }

        return valid;
    }

    private boolean isValid(ConstraintValidatorContext context, boolean valid, String countryCode, String phone, String secondPhone, String secondCountryCode) {
        valid &= validatePhone(countryCode, phone, context, "phone");

        if (secondPhone != null && !secondPhone.isBlank()) {
            String secondCode = (secondCountryCode != null && !secondCountryCode.isBlank())
                    ? secondCountryCode
                    : countryCode;
            valid &= validatePhone(secondCode, secondPhone, context, "secondPhone");
        }
        return valid;
    }

    private boolean validatePhone(String countryCode, String phone, ConstraintValidatorContext context, String fieldName) {
        if (countryCode == null || phone == null) return false;

        String fullNumber = countryCode + phone;

        try {
            Phonenumber.PhoneNumber parsed = phoneUtil.parse(fullNumber, null);
            if (!phoneUtil.isValidNumber(parsed)) {
                buildViolation(context, fieldName, fullNumber);
                return false;
            }
        } catch (NumberParseException e) {
            buildViolation(context, fieldName, fullNumber);
            return false;
        }

        return true;
    }

    private void buildViolation(ConstraintValidatorContext context, String field, Object... args) {
        context.disableDefaultConstraintViolation();

        StringBuilder messageTemplate = new StringBuilder("{" + "error.phone.service.invalid_format");
        for (Object arg : args) {
            messageTemplate.append(",").append(arg);
        }
        messageTemplate.append("}");

        context.buildConstraintViolationWithTemplate(messageTemplate.toString())
                .addPropertyNode(field)
                .addConstraintViolation();
    }
}
