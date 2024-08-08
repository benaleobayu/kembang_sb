package com.bca.byc.model.api;

import com.bca.byc.model.cms.BusinessCategoryDetailResponse;
import com.bca.byc.model.cms.InterestCategoryDetailResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InterestDetailResponse {

    private Long id;

    private String name;

    private String province;

    private String lineOfBusiness;

    private String address;

    private String website;

    private String description;

    private List<InterestCategoryDetailResponse> categories;

    private Integer orders;

    private Boolean status;

    private String createdAt;

    private String updatedAt;

}
