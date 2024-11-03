package com.bca.byc.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GlobalAttributeService {

    List<Map<String, List<?>>> listAttributeUserManagement();

    List<Map<String, List<?>>> listAttributeRole();

    List<Map<String, List<?>>> listAttributeChannel();

    List<Map<String, List<?>>> listAttributeCustomer();
}
