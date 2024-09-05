package com.bca.byc.model;

import com.bca.byc.entity.RoleHasPermission;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Data
public class RoleDetailResponse {

    private Long id;
    private String name;
    private List<RoleHasPermission> permissions;

}
