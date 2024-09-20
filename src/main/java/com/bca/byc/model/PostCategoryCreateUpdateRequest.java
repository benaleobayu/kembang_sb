package com.bca.byc.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostCategoryCreateUpdateRequest {

    @NotBlank
    private String name;
    private String icon;
    private String description;
    private Boolean isActive;
}
