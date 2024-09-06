package com.bca.byc.model;

import com.bca.byc.entity.Permission;
import com.bca.byc.entity.RoleHasPermission;
import com.bca.byc.response.PermissionResponse;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class AdminCmsDetailResponse {
    private Long id;
    private String name;
    private String email;
    private String roleName;
    private Map<String, List<PermissionResponse>> permissions; // Map<permission, List<SimpleGrantedAuthority>>


}
