package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class KanwilDetailResponse extends AdminModelBaseDTOResponse {

    private String code;

    private String name;

    private String address;

    private String phone;

    private String locationId;

    private String locationName;

    private Boolean status;

}
