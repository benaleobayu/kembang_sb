package com.bca.byc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpectItemCreateRequest {

    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;

    private String description;

    @NotBlank(message = "Order is mandatory")
    private Integer orders;

    @NotBlank(message = "Status is mandatory")
    private Boolean status;

    // relation
    @NotBlank(message = "Expect Category Id is mandatory")
    private Long expectCategoryId;


}
