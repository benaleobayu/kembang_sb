package com.kembang.validator;


import com.kembang.validator.annotation.MemberTypeEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class MemberTypeValidator implements ConstraintValidator<MemberTypeEnum, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        List<String> values = Arrays.asList("SOLITAIRE", "PRIORITY", "NOT_MEMBER");

        return values.contains(value);
    }
}