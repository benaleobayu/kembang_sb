package com.bca.byc.model;

import lombok.Data;

import java.util.List;

@Data
public class OnboardingBusinessRequest {
    private String businessName;
    private String businessAddress;
    private List<OnboardingBusinessCategoryRequest> businessCategories;
}
