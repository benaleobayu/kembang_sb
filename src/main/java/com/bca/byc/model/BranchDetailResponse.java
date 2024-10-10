package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BranchDetailResponse extends AdminModelBaseDTOResponse<Long>{

    private String code;

    private String name;

    private String address;

    private String phone;

    private String locationId;

    private String locationName;

    private String kanwilId;

    private String kanwilName;

    private Boolean status;
}
