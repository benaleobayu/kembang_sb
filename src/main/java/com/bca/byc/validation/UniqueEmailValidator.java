package com.bca.byc.validation;

import com.bca.byc.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private static final Logger logger = LogManager.getLogger(UniqueEmailValidator.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            logger.debug("Email is null.");
            return true; // or false, depending on your needs
        }

        boolean exists = userRepository.existsByEmail(email);
        logger.debug("Checking if email {} exists: {}", email, exists);
        
        return !exists;
    }
}
