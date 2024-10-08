package com.bca.byc.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class BusinessTreeRequest {
    @NotBlank(message = "Business Name is required")
    private String businessName;
    @NotBlank(message = "Business Address is required")
    private String businessAddress;

    private Boolean isPrimary;

    private List<String> categoryItemIds;
    private List<String> locationIds;
}
