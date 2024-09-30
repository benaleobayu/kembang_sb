package com.bca.byc.model.data;

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

    @Data
    public static class BusinessCategoryResponse {
        private String id;
        private Long index;
        private String name;
    }

    @Data
    public static class LocationListResponse {
        private String id;
        private Long index;
        private String name;
    }
}
