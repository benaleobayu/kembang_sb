package com.bca.byc.model;

import com.bca.byc.entity.Location;
import lombok.Data;

@Data
public class BranchCreateUpdateRequest {

    private String code;

    private String name;

    private String address;

    private String phone;

    private Long location;

    private Boolean status;

}
