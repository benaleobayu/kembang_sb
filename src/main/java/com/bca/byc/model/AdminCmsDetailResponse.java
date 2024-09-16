package com.bca.byc.model;

import com.bca.byc.response.PermissionResponse;
import lombok.Data;

import java.util.List;

@Data
public class AdminCmsDetailResponse {
    private Long id;
    private String name;
    private String email;
    private String roleName;
    private List<MenuName> permissions; // Map<permission, List<SimpleGrantedAuthority>>


    @Data
    public static class MenuName {
        private String menuName;
        private List<PermissionResponse> permissions;
    }
}
