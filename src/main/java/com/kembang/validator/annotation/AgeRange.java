package com.kembang.validator.annotation;

import com.kembang.validator.AgeRangeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeRangeValidator.class)
public @interface AgeRange {
    String message() default "Age must be within the allowed range";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}