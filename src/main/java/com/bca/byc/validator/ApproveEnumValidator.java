package com.bca.byc.validator;


import com.bca.byc.validator.annotation.ApproveEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class ApproveEnumValidator implements ConstraintValidator<ApproveEnum, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        List<String> values = Arrays.asList("SYSTEM", "ADMIN_SPV", "ADMIN_OPT", "SUPERADMIN");

        return values.contains(value);
    }
}