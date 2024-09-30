package com.bca.byc.model;

import com.bca.byc.response.PermissionResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionListResponse {
    private String menuName;
    private List<PermissionResponse> permissions;
}
