package com.bca.byc.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class BusinessCategoryDetailResponse {

    private Long id;
    private String name;
    private String description;
    private Integer orders;
    private Boolean status;
    private String createdAt;
    private String updatedAt;

    private Long parentId;

    private Set<BusinessCategoryDetailResponse> children = new HashSet<>();

}