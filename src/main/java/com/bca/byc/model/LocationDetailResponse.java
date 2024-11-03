package com.bca.byc.model;

import lombok.Data;

@Data
public class LocationDetailResponse extends ModelBaseDTOResponse {

    private String name;
    private String address;
    private String description;
    private Integer orders;
    private Boolean status;
}
