package com.bca.byc.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminDetailResponse extends AdminModelBaseDTOResponse<Long>{
    private String avatar;
    private String cover;
    private String name;
    private String email;
    private String roleId;
    private String roleName;
    private String type;
    private Boolean status;
    private Boolean isVisible;

    private List<CastIdAndNameResponse> accounts;

    private Boolean isHaveContent;
}

