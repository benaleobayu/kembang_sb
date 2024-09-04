package com.bca.byc.validator.annotation;

import com.bca.byc.validator.ApproveEnumValidator;
import com.bca.byc.validator.MemberTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MemberTypeValidator.class)
public @interface MemberTypeEnum {
    String message() default "Invalid value must be one of: SOLITAIRE, PRIORITY, NOT_MEMBER";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

