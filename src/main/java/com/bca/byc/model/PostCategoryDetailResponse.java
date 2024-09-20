package com.bca.byc.model;

import lombok.Data;

@Data
public class PostCategoryDetailResponse {

    private Long id;
    private String name;
    private String icon;
    private String description;
    private Boolean isActive;
    private Boolean isDeleted;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;

}
