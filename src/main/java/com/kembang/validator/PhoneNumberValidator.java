package com.kembang.validator;

import com.kembang.validator.annotation.PhoneNumberValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberValidation, String> {

    private static final String PHONE_NUMBER_PATTERN = "^\\+?[0-9]{7,15}$";


    @Override
    public void initialize(PhoneNumberValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        return phoneNumber != null && phoneNumber.matches(PHONE_NUMBER_PATTERN);
    }
}
