package com.bca.byc.validator;

import com.bca.byc.validator.annotation.AgeRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AgeRangeValidator implements ConstraintValidator<AgeRange, LocalDate> {

//    @Autowired
//    private SettingsRepository repository;

    private int minAge = 18;
    private int maxAge = 35;

    @Override
    public void initialize(AgeRange constraintAnnotation) {
        this.minAge = 18;
        this.maxAge = 35;
    }
//    @Override
//    public void initialize(AgeRange constraintAnnotation) {
//        this.minAge = repository.findByIdentity("MIN_AGE").map(Settings::getValue).orElse(18);
//        this.maxAge = repository.findByIdentity("MAX_AGE").map(Settings::getValue).orElse(35);
//    }

    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext context) {
        if (birthdate == null) {
            return true;
        }

        LocalDate today = LocalDate.now();
        int age = Period.between(birthdate, today).getYears();
        return age >= minAge && age <= maxAge;
    }
}