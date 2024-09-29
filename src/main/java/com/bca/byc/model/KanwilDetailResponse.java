package com.bca.byc.model;

import lombok.Data;

@Data
public class KanwilDetailResponse extends AdminModelBaseDTOResponse {

    private String code;

    private String name;

    private String address;

    private String phone;

    private String location;

    private Boolean status;

}
