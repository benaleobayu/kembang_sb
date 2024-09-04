package com.bca.byc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SettingsCreateRequest {

    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;

    @NotBlank(message = "Identity is mandatory")
    @Size(max = 50, message = "Identity must be less than 50 characters")
    private String identity;

    private String description;
    private Integer value;

    @NotBlank(message = "Status is mandatory")
    private Boolean status;
}
