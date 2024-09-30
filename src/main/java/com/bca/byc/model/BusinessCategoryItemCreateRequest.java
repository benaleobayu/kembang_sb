package com.bca.byc.model;

import lombok.Data;

@Data
public class BusinessCategoryItemCreateRequest {

    private String name;

    private String description;

    private Integer orders;

}
