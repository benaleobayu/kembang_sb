package com.bca.byc.service;

import com.bca.byc.model.PermissionListResponse;

import java.util.List;

public interface PermissionService {

    List<PermissionListResponse> findAllData();
}
