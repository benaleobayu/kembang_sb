package com.bca.byc.response;

import lombok.Data;

@Data
public class PermissionResponse {
    private Long permissionId;
    private String permissionName;
    private Boolean disabled;
}
