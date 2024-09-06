package com.bca.byc.service;

import com.bca.byc.response.PermissionResponse;

import java.util.List;
import java.util.Map;

public interface PermissionService {

    Map<String, List<PermissionResponse>> findAllData();
}
