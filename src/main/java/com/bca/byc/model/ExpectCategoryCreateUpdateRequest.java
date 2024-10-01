package com.bca.byc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ExpectCategoryCreateUpdateRequest {

    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;

    private String description;

    @NotNull(message = "Order is mandatory")
    private Integer orders;

    @NotNull(message = "Status is mandatory")
    private Boolean status;

}
