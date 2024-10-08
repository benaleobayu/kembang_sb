package com.bca.byc.model;

import lombok.Data;

@Data
public class FaqCategoryCreateUpdateRequest {

    private String name;
    private String description;
    private Boolean status;
    private Integer orders;

}
