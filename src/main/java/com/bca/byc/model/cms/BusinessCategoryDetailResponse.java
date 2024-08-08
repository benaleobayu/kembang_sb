package com.bca.byc.model.cms;

import lombok.Data;

@Data
public class BusinessCategoryDetailResponse {

    private Long id;

    private String name;

    private String description;

    private Integer orders;

    private Boolean status;

    private String createdAt;

    private String updatedAt;
}
