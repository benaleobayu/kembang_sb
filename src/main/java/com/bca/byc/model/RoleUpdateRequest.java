package com.bca.byc.model;

import com.bca.byc.entity.Permission;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class RoleUpdateRequest{

    @NotBlank(message = "Name is required")
    private String name;

    private List<Long> addPermissionIds;

    private List<Long> removePermissionIds;
}
