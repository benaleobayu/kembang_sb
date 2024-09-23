package com.bca.byc.service;

import java.util.List;
import java.util.Map;

public interface UserManagementService {
    List<Map<String, List<?>>> listAttributePreRegister();

    List<Map<String, List<?>>> listAttributeCreateUpdatePreRegister();

    List<Map<String, List<?>>> listAttributeUserActive();

}
