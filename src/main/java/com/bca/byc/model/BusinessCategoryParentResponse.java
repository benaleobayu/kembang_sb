package com.bca.byc.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BusinessCategoryParentResponse {
    private String id;
    private Long index;
    private String name;
    private List<BusinessCategoryResponse> subCategories = new ArrayList<>();
}
