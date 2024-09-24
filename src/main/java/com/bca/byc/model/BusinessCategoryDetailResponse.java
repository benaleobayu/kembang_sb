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

    public BusinessCategoryDetailResponse() {
    }

    public BusinessCategoryDetailResponse(Long id, String name, Long parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public BusinessCategoryDetailResponse(Long id, String name, Boolean status, Set<BusinessCategoryDetailResponse> children) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.children = children;
    }
}
