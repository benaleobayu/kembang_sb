package com.bca.byc.model;

import lombok.Data;

@Data
public class BusinessCatalogListResponse<S> {

    private String id;
    private S Index;
    private String title;
    private String description;
    private String image;

}
