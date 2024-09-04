package com.bca.byc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FaqCategoryCreateRequest {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
    private String name;

    private String description;

    @NotBlank(message = "Order is mandatory")
    private Integer orders;

    @NotBlank(message = "Status is mandatory")
    private Boolean status;
}
