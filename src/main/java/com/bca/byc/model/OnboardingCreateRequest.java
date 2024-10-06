package com.bca.byc.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class OnboardingCreateRequest {
    // business
    private List<BusinessTreeRequest> businesses;
    // expect
    private List<ExpectCategoryTreeRequest> expectCategories;
}