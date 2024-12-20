package com.kembang.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LocationIndexResponse extends ModelBaseDTOResponse {

    private String name;
    private String province;
    private Integer orders;
    private Boolean status;

}
