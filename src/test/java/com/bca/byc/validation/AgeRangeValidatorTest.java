package com.bca.byc.validation;

import com.bca.byc.service.validator.AgeRangeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AgeRangeValidatorTest {

    @InjectMocks
    private AgeRangeValidator validator;

    @Mock
    private AgeRangeService ageRangeService;

    @Test
    public void testValidAge() {
        MockitoAnnotations.openMocks(this);
        when(ageRangeService.getMinAge()).thenReturn(18);
        when(ageRangeService.getMaxAge()).thenReturn(35);

        LocalDate validBirthdate = LocalDate.of(2000, 01, 02);
        assertTrue(validator.isValid(validBirthdate, null));
    }

    @Test
    public void testInvalidAge() {
        MockitoAnnotations.openMocks(this);
        when(ageRangeService.getMinAge()).thenReturn(18);
        when(ageRangeService.getMaxAge()).thenReturn(35);

        LocalDate invalidBirthdate = LocalDate.now().minusYears(40);
        assertFalse(validator.isValid(invalidBirthdate, null));
    }
}