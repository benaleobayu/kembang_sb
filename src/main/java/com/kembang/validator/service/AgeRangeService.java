package com.kembang.validator.service;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Data
@Component("ageRangeService")
public class AgeRangeService {

    private int minAge = 18;  // Default value
    private int maxAge = 35;  // Default value

}
