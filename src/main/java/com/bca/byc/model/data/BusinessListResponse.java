package com.bca.byc.model.data;

import com.bca.byc.entity.ExpectCategory;
import com.bca.byc.model.ExpectCategoryDetailResponse;
import com.bca.byc.model.UserInfoResponse;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BusinessListResponse {
    private Long id;
    private String name;
    private String address;
    private String lineOfBusiness;
    private Boolean isPrimary;

    private List<LocationListResponse> locations = new ArrayList<>();

    private List<BusinessCategoryResponse> subCategories = new ArrayList<>();

    @Data
    public static class BusinessCategoryResponse {
        private Long id;
        private String name;
    }

    @Data
    public static class LocationListResponse {
        private Long id;
        private String name;
    }
}
