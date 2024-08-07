package com.bca.byc.model.api;

import lombok.Data;

@Data
public class BusinessDetailResponse {

    private Long id;

    private String name;

    private String province;

    private String line_of_business;

    private String address;

    private String cin;

    private String website;

    private String description;

    private String orders;

    private Boolean status;

    private String createdAt;

    private String updatedAt;

}
