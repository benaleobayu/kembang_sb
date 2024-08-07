package com.bca.byc.model.cms;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
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
