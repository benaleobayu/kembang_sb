package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LocationIndexResponse extends ModelBaseDTOResponse {

    private String name;
    private String address;
    private Integer orders;
    private Boolean status;

}
