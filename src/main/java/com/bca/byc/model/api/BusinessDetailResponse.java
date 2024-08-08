package com.bca.byc.model.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BusinessDetailResponse {

    private Long id;

    private String name;

    private String province;

    private String lineOfBusiness;

    private String address;

    private String website;

    private String description;

    private Integer orders;

    private Boolean status;

    private String createdAt;

    private String updatedAt;

}
