package com.bca.byc.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BusinessDetailResponse {
    private String id;
    private Long index;
    private String name;
    private String address;

    private BusinessCategoryParentResponse category;
    private List<LocationListResponse> locations = new ArrayList<>();
    private Integer totalCatalogs;

}
