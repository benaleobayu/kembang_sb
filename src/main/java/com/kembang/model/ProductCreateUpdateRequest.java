package com.kembang.model;

import lombok.Data;

@Data
public class ProductCreateUpdateRequest {

    private String name;

    private String description;

    private String categoryId;

    private Integer price;

    private Boolean isActive;

}
