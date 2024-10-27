package com.bca.byc.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GlobalAttributeService {
    List<Map<String, List<?>>> listAttributePreRegister();

    List<Map<String, List<?>>> listAttributeCreateUpdatePreRegister();

    List<Map<String, List<?>>> listAttributeUserManagement();

    List<Map<String, List<?>>> listAttributeSubBusinessCategory();

    List<Map<String, List<?>>> listAttributeRole();

    List<Map<String, List<?>>> listAttributeChannel();

    List<Map<String, List<?>>> listStatusTypeReportContentComment();

    // -------------------------------------

    void makeUserBulkDeleteTrue(Set<String> ids);

    void makeUserBulkHardDeleteTrue(Set<String> ids);

}
