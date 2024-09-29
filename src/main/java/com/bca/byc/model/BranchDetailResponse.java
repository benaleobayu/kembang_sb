package com.bca.byc.model;

import lombok.Data;

@Data
public class BranchDetailResponse extends AdminModelBaseDTOResponse{

    private String code;

    private String name;

    private String address;

    private String phone;

    private String location;

    private String kanwil;

    private Boolean status;
}
