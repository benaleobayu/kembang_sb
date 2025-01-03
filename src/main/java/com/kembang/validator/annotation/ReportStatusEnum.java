package com.kembang.validator.annotation;

import com.kembang.validator.ReportStatusValidator;
import com.kembang.validator.ReportTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ReportStatusValidator.class)
public @interface ReportStatusEnum {
    String message() default "Invalid value must be one of: DRAFT, REVIEW, REJECT, TAKE_DOWN";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

