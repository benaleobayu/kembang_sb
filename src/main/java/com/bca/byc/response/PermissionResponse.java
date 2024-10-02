package com.bca.byc.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse {
    private Long permissionId;
    private String permissionName;
    private Boolean disabled;
    private Boolean active;
}
