package com.bca.byc.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BusinessCategoryTreeRequest {
    @NotBlank(message = "Business Category is required")
    private String businessCategoryId;
    @NotBlank(message = "Business Category Child is required")
    private String businessCategoryChildId;
}
