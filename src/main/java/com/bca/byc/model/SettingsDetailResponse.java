package com.bca.byc.model;

import lombok.Data;


@Data
public class SettingsDetailResponse {

    private Long id;
    private String name;
    private String identity;
    private String description;
    private Integer value;
    private Boolean status;
    private String createdAt;
    private String updatedAt;

}

