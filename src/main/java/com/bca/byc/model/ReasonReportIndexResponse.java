package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReasonReportIndexResponse extends AdminModelBaseDTOResponse<Integer> {

    private String id;
    private Integer index;
    private String icon;
    private String name;
    private Boolean status;
    private Boolean isRequired;

}
