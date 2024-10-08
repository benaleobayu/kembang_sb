package com.bca.byc.model.data;

import com.bca.byc.model.BusinessCategoryResponse;
import com.bca.byc.model.LocationListResponse;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BusinessListResponse {
    private String id;
    private Long index;
    private String name;
    private String address;
    private String lineOfBusiness;
    private Boolean isPrimary;

    private List<LocationListResponse> locations = new ArrayList<>();

    private List<BusinessCategoryResponse> subCategories = new ArrayList<>();

    private Integer totalCatalogs;

}
