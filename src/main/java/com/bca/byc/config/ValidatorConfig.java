package com.bca.byc.config;

import com.bca.byc.service.validator.AgeRangeService;
import com.bca.byc.validation.AgeRangeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorConfig {

    @Bean
    public AgeRangeValidator ageRangeValidator(AgeRangeService ageRangeService){
        return new AgeRangeValidator(ageRangeService);
    }
}
