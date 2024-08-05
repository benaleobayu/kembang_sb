package com.bca.byc.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberValidation, String> {

    private static final String PHONE_NUMBER_PATTERN = "^\\+?[0-9]{10,15}$";


    @Override
    public void initialize(PhoneNumberValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        return phoneNumber != null && phoneNumber.matches(PHONE_NUMBER_PATTERN);
    }
}
