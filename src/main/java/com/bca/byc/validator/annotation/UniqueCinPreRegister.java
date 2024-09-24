package com.bca.byc.validator.annotation;
import com.bca.byc.validator.UniqueCinPreRegisterValidator;
import com.bca.byc.validator.UniqueEmailPreRegisterValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueCinPreRegisterValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueCinPreRegister {
    String message() default "Cin data already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
