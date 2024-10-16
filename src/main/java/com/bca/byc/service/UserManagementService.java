package com.bca.byc.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserManagementService {
    List<Map<String, List<?>>> listAttributePreRegister();

    List<Map<String, List<?>>> listAttributeCreateUpdatePreRegister();

    List<Map<String, List<?>>> listAttributeUserManagement();

    List<Map<String, List<?>>> listAttributeSubBusinessCategory();


    // -------------------------------------

    void makeUserBulkDeleteTrue(Set<String> ids);
}
