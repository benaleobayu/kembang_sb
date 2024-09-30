package com.bca.byc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RoleCreateUpdateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Status is required")
    private Boolean status;

    private List<PrivilegeRoleCreateUpdateRequest> permissions;


}
