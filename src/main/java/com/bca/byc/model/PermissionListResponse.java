package com.bca.byc.model;

import com.bca.byc.response.PermissionResponse;
import lombok.Data;

import java.util.List;

@Data
public class PermissionListResponse {
    private String menuName;
    private List<PermissionResponse> permissions;
}
