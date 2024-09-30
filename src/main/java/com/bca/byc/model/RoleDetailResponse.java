package com.bca.byc.model;

import com.bca.byc.response.PermissionResponse;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RoleDetailResponse {

    private String id;
    private Long index;
    private String name;
    private List<PermissionListResponse> permissions; // Map<permission, List<SimpleGrantedAuthority>>
    private Boolean status;


}

