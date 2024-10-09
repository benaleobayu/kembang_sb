package com.bca.byc.model;

import lombok.Data;

@Data
public class LocationCreateUpdateRequest {

    private String name;
    private String province;
    private String address;
    private String order;
    private Boolean status;

}
