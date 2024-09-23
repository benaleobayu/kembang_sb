package com.bca.byc.service.impl;

import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.model.attribute.AttributeResponse;
import com.bca.byc.repository.LocationRepository;
import com.bca.byc.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private final LocationRepository locationRepository;


    @Override
    public List<Map<String, List<?>>> listAttributePreRegister() {
        List<String> statuses = Arrays.stream(AdminApprovalStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        List<AttributeResponse> listStatusResponse = statuses.stream()
                .map((c) -> {
                    AttributeResponse response = new AttributeResponse();
                    response.setId(statuses.indexOf(c));
                    response.setName(c);
                    return response;
                })
                .collect(Collectors.toList());

        List<Map<String, List<?>>> attributes = new ArrayList<>();

        Map<String, List<?>> listStatus = new HashMap<>();
        listStatus.put("status", listStatusResponse);
        attributes.add(listStatus);

        return attributes;
    }

    @Override
    public List<Map<String, List<?>>> listAttributeUserActive() {
        return null;
    }
}
