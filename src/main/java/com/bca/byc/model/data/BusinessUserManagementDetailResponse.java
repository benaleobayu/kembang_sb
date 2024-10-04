package com.bca.byc.model.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BusinessUserManagementDetailResponse {
    private String id;
    private Long index;
    private String name;
    private List<BusinessCategoryResponse> subCategories = new ArrayList<>();

    @Data
    public static class BusinessCategoryResponse {
        private String id;
        private Long index;
        private String name;
    }
}
