package com.kembang.model;

import lombok.Data;

@Data
public class LocationCreateUpdateRequest {

    private String name;
    private String province;
    private Integer orders;
    private Boolean status;

}
