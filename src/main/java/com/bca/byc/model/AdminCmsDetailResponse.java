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
    private List<PermissionListResponse> permissions; // Map<permission, List<SimpleGrantedAuthority>>



}