package com.bca.byc.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeRangeValidator.class)
public @interface AgeRange {
    String message() default "Age must be between {min} and {max} years";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}