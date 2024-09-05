package com.bca.byc.model;

import com.bca.byc.entity.Permission;
import com.bca.byc.entity.RoleHasPermission;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class AdminCmsDetailResponse {
    private Long id;
    private String name;
    private String email;
    private String roleName;
    private List<String> permissions;

}
