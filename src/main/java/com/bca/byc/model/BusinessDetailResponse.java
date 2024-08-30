package com.bca.byc.model;

import lombok.Data;

import java.util.List;

@Data
public class BusinessDetailResponse {
    private Long userId;

    private Long id;
    private String name;
    private String province;
    private String lineOfBusiness;
    private String address;
    private String website;
    private String description;
    private List<LocationModelDTO.LocationDetailResponse> locations;

    private List<BusinessCategoryModelDTO.BusinessCategoryDetailResponse> categories;

    private Integer orders;

    private Boolean status;

    private String createdAt;

    private String updatedAt;

}


