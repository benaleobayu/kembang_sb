package com.bca.byc.service.impl;

import com.bca.byc.entity.Location;
import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.enums.UserType;
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
        // status approval
        List<String> statuses = Arrays.stream(AdminApprovalStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        List<AttributeResponse> listStatusResponse = statuses.stream()
                .map((c) -> {
                    AttributeResponse response = new AttributeResponse<>();
                    response.setId((long) statuses.indexOf(c));
                    response.setValue(c);
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
    public List<Map<String, List<?>>> listAttributeCreateUpdatePreRegister() {
        List<Map<String, List<?>>> attributes = new ArrayList<>();


        // Data UserTypes
        List<AttributeResponse<String>> listUserTypes = Arrays.asList(
                new AttributeResponse<>((long) UserType.MEMBER_SOLITAIRE.ordinal(), UserType.MEMBER_SOLITAIRE.name(), "Solitaire"),
                new AttributeResponse<>((long) UserType.MEMBER_PRIORITY.ordinal(), UserType.MEMBER_PRIORITY.name(), "Priority"),
                new AttributeResponse<>((long) UserType.NOT_MEMBER.ordinal(), UserType.NOT_MEMBER.name(), "Non Member")
        );
        Map<String, List<?>> userTypeMap = new HashMap<>();
        userTypeMap.put("segmentation", listUserTypes);
        attributes.add(userTypeMap);

        return attributes;
    }

    @Override
    public List<Map<String, List<?>>> listAttributeUserActive() {
        List<Location> locations = locationRepository.findAll().stream()
                .map(l -> {
                    Location location = new Location();
                    location.setId(l.getId());
                    location.setName(l.getName());
                    return location;
                }).collect(Collectors.toList());

        List<AttributeResponse> listStatusResponse = locations.stream()
                .map((c) -> {
                    AttributeResponse response = new AttributeResponse<>();
                    response.setId(c.getId());
                    response.setValue(c.getName());
                    response.setName(c.getName());
                    return response;
                })
                .collect(Collectors.toList());

        List<Map<String, List<?>>> attributes = new ArrayList<>();

        Map<String, List<?>> listStatus = new HashMap<>();
        listStatus.put("status", listStatusResponse);
        attributes.add(listStatus);

        return attributes;
    }
}
