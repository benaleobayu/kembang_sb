package com.kembang.validator.annotation;

import com.kembang.validator.ReportTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ReportTypeValidator.class)
public @interface ReportTypeEnum {
    String message() default "Invalid value must be one of: POST, COMMENT, COMMENT_REPLY, USER";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

