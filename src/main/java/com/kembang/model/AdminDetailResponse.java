package com.kembang.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdminDetailResponse extends ModelBaseDTOResponse<Long> {
    private String avatar;
    private String cover;
    private String name;
    private String email;
    private String roleId;
    private String roleName;
    private String type;
    private Boolean status;
    private Boolean isVisible;

    private Boolean isHaveContent;
}

