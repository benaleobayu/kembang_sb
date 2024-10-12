package com.bca.byc.validator;


import com.bca.byc.validator.annotation.ReportStatusEnum;
import com.bca.byc.validator.annotation.ReportTypeEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class ReportStatusValidator implements ConstraintValidator<ReportStatusEnum, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        List<String> values = Arrays.asList("DRAFT", "REVIEW", "REJECT", "TAKE_DOWN");

        return values.contains(value);
    }
}