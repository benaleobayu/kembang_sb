package com.bca.byc.validation;

import com.bca.byc.service.validator.AgeRangeService;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class AgeRangeValidatorTest {

    @Mock
    private AgeRangeService ageRangeService;

    @Mock
    private ConstraintValidatorContext context;

    @InjectMocks
    private AgeRangeValidator ageRangeValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock the min and max age values
        when(ageRangeService.getMinAge()).thenReturn(18);
        when(ageRangeService.getMaxAge()).thenReturn(35);

        // Initialize the validator with the mocked service
        ageRangeValidator.initialize(null);
    }

    @Test
    void testIsValid_WithValidAge() {
        // Arrange
        LocalDate validBirthdate = LocalDate.now().minusYears(30);

        // Act
        boolean isValid = ageRangeValidator.isValid(validBirthdate, context);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testIsValid_WithTooYoungAge() {
        // Arrange
        LocalDate youngBirthdate = LocalDate.now().minusYears(10);

        // Act
        boolean isValid = ageRangeValidator.isValid(youngBirthdate, context);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testIsValid_WithTooOldAge() {
        // Arrange
        LocalDate oldBirthdate = LocalDate.now().minusYears(70);

        // Act
        boolean isValid = ageRangeValidator.isValid(oldBirthdate, context);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testIsValid_WithNullBirthdate() {
        // Act
        boolean isValid = ageRangeValidator.isValid(null, context);

        // Assert
        assertTrue(isValid);
    }
}