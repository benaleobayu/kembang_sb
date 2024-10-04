package com.bca.byc.validator;


import com.bca.byc.validator.annotation.ReportTypeEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class ReportTypeValidator implements ConstraintValidator<ReportTypeEnum, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        List<String> values = Arrays.asList("POST", "COMMENT", "COMMENT_REPLIES", "USER");

        return values.contains(value);
    }
}