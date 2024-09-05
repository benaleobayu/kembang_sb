package com.bca.byc.service;

import com.bca.byc.entity.Role;
import com.bca.byc.model.RoleDetailResponse;

import java.util.List;
import java.util.Map;

public interface PermissionService {

    Map<String, List<RoleDetailResponse.PermissionResponse>> findAllData();
}
