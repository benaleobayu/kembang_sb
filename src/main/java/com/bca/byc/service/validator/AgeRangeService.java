package com.bca.byc.service.validator;

import lombok.Data;
import org.springframework.stereotype.Service;

import javax.inject.Named;

@Service
@Data
@Named("ageRangeService")
public class AgeRangeService {

    private int minAge = 18;  // Default value
    private int maxAge = 35;  // Default value

}
