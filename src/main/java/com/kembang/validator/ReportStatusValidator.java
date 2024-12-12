package com.kembang.validator;


import com.kembang.validator.annotation.ReportStatusEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class ReportStatusValidator implements ConstraintValidator<ReportStatusEnum, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        List<String> values = Arrays.asList("NULL", "DRAFT", "REVIEW", "REJECT", "TAKE_DOWN", "SUSPENDED", "WARNING", "END");

        return values.contains(value);
    }
}