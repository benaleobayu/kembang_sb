package com.bca.byc.service;

import com.bca.byc.entity.Permission;

import java.util.List;

public record RoleCreateRequest(

        String name,

        List<Permission> permissions

) {
}
