package com.bca.byc.model.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BusinessCreateRequest {

    @NotBlank(message = "User id is required")
    private Integer userId;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    private String province;

    private String lineOfBusiness;

    private String address;

    private String website;

    private String description;

    @NotBlank(message = "Order is required")
    private String orders;

    @NotBlank(message = "Status is required")
    private Boolean status;

}
