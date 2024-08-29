package com.bca.byc.validation;
import com.bca.byc.service.validator.AgeRangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

@Component
public class AgeRangeValidator implements ConstraintValidator<AgeRange, LocalDate> {

    private final AgeRangeService ageRangeService;

    @Autowired
    public AgeRangeValidator(AgeRangeService ageRangeService) {
        this.ageRangeService = ageRangeService;
    }

    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext context) {
        if (birthdate == null) {
            return true; // Return true if birthdate is null, as it is not the responsibility of this validator to handle null values
        }

        int age = Period.between(birthdate, LocalDate.now()).getYears();
        return age >= 18 && age <= 35;
    }
}