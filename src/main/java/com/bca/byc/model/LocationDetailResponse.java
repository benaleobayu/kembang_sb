package com.bca.byc.model;

import lombok.Data;

@Data
public class LocationDetailResponse {

    private Long id;
    private String name;
    private String address;
    private String description;
    private Integer orders;
    private Boolean status;
    private String createdAt;
    private String updatedAt;
}
