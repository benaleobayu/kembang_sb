package com.bca.byc.model;

import com.bca.byc.entity.Permission;

import java.util.List;

public record RoleCreateRequest(

        String name,

        Boolean status,

        List<Permission> permissions

) {
}
