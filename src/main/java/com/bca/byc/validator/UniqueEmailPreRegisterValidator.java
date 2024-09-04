package com.bca.byc.validator;

import com.bca.byc.repository.PreRegisterRepository;
import com.bca.byc.validator.annotation.UniqueEmailPreRegister;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueEmailPreRegisterValidator implements ConstraintValidator<UniqueEmailPreRegister, String> {

    @Autowired
    private PreRegisterRepository repository;

    @Override
    public void initialize(UniqueEmailPreRegister constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isEmpty()) {
            return true; // @NotNull/@NotEmpty should handle this
        }
        return !repository.existsByEmail(email);
    }
}
