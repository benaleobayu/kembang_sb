package com.kembang.model;

import lombok.Data;

@Data
public class LocationDetailResponse extends ModelBaseDTOResponse {

    private String name;
    private String province;
    private String description;
    private Integer orders;
    private Boolean status;
}
