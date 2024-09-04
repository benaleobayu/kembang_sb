package com.bca.byc.validator.annotation;

import com.bca.byc.validator.ApproveEnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ApproveEnumValidator.class)
public @interface ApproveEnum {
    String message() default "Invalid value must be one of: SYSTEM, ADMIN_SPV, ADMIN_OPT, SUPERADMIN";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

