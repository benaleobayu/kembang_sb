package com.bca.byc.model;

import com.bca.byc.response.PermissionResponse;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RoleDetailResponse {

    private Integer id;
    private String name;
    private Map<String, List<PermissionResponse>> permissions; // Map<permission, List<SimpleGrantedAuthority>>
    private Boolean status;


}

