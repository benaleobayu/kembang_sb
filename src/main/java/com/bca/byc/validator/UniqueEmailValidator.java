package com.bca.byc.validator;

import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.validator.annotation.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private AppUserRepository userRepository;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isEmpty()) {
            return true; // @NotNull/@NotEmpty should handle this
        }
        return !userRepository.existsByEmail(email);
    }
}
