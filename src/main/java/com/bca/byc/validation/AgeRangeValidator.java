package com.bca.byc.validation;
import com.bca.byc.service.validator.AgeRangeService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AgeRangeValidator implements ConstraintValidator<AgeRange, LocalDate> {

    private final AgeRangeService ageRangeService;

    @Autowired
    public AgeRangeValidator(AgeRangeService ageRangeService) {
        this.ageRangeService = ageRangeService;
    }

    @Override
    public void initialize(AgeRange constraintAnnotation) {
        // Initialization logic if needed
    }

    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext context) {
        if (birthdate == null) {
            return true; // Consider how to handle null values
        }

        try {
            int minAge = ageRangeService.getMinAge();
            int maxAge = ageRangeService.getMaxAge();
            Period age = Period.between(birthdate, LocalDate.now());

            if (age.getYears() < minAge || age.getYears() > maxAge) {
                return false;
            } else if (age.getYears() == maxAge) {
                // Ensure they haven't exceeded the max age by days
                LocalDate maxAgeDate = birthdate.plusYears(maxAge);
                return !LocalDate.now().isAfter(maxAgeDate);
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during age validation", e);
        }
    }
}