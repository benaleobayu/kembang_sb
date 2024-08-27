package com.bca.byc.service.validator;

import lombok.Data;
import org.springframework.stereotype.Service;

@Service
public class AgeRangeService {

    private int minAge = 18;  // Default value
    private int maxAge = 35;  // Default value

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

}
