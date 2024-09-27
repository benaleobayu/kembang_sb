package com.bca.byc.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class RoleCreateUpdateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private Boolean status;

    private List<PrivilegeRoleCreateUpdateRequest> permissions;


}
