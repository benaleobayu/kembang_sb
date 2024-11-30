package com.bca.byc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDetailResponse{

    private String id;

    private String name;

    private String description;

    private String image;

    private String category;

    private Integer price;

    private Boolean isActive;

}
