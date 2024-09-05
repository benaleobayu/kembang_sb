package com.bca.byc.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RoleDetailResponse {

    private Integer id;
    private String name;
    private Map<String, List<PermissionResponse>> permissions; // Map<permission, List<SimpleGrantedAuthority>>

    @Data
    public static class PermissionResponse {
        private Long permissionId;
        private String permissionName;
        private Boolean disabled;
    }
}
