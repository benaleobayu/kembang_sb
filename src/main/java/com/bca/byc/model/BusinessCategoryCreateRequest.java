package com.bca.byc.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BusinessCategoryCreateRequest {

    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Name must be less than 50 characters")
    @Schema(example = "Business Category 1")
    private String name;

    private String description;

    @NotBlank(message = "Order is mandatory")
    @Schema(example = "1")
    private Integer orders;

    @NotBlank(message = "Status is mandatory")
    @Schema(example = "true | false")
    private Boolean status;

    private Long checkParentId;

}
