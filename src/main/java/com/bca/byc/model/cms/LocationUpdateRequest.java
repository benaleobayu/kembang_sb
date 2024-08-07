package com.bca.byc.model.cms;

import lombok.Data;

@Data
public class LocationUpdateRequest {

    private String name;
    private String description;
    private Integer orders;
    private Boolean status;

}
