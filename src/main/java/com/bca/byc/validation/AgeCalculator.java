package com.bca.byc.validation;

import java.time.LocalDate;
import java.time.Period;

public class AgeCalculator {

    public static Period calculateAge(LocalDate birthdate) {
        return Period.between(birthdate, LocalDate.now());
    }
}
