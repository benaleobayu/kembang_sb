package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleListResponse extends AdminModelBaseDTOResponse {
    private String name;
    private Boolean status;
    private Integer orders;
}
