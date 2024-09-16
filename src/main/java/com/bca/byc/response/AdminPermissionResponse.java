package com.bca.byc.response;

import lombok.Data;

import java.util.Set;

@Data
public class AdminPermissionResponse {

    private Set<String> permissions;

}