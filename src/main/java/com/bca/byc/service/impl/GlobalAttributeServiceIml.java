package com.bca.byc.service.impl;

import com.bca.byc.enums.UserType;
import com.bca.byc.model.attribute.AttributeResponse;
import com.bca.byc.repository.LocationRepository;
import com.bca.byc.repository.RoleRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.service.GlobalAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GlobalAttributeServiceIml implements GlobalAttributeService {

    private final AppAdminRepository adminRepository;

    private final LocationRepository locationRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<Map<String, List<?>>> listAttributeUserManagement() {
        List<Map<String, List<?>>> attributes = new ArrayList<>();
        Map<String, List<?>> listStatus = new HashMap<>();
        listStatus.put("Location", getLocationList());
        listStatus.put("Segmentation", getUserTypeList());
        listStatus.put("Senior", getSeniorityList());
        attributes.add(listStatus);

        return attributes;
    }

    @Override
    public List<Map<String, List<?>>> listAttributeRole() {
        List<Map<String, List<?>>> attributes = new ArrayList<>();
        Map<String, List<?>> listAttr = new HashMap<>();
        listAttr.put("role", getRoleList());
        listAttr.put("status", getStatusList());
        attributes.add(listAttr);

        return attributes;
    }

    @Override
    public List<Map<String, List<?>>> listAttributeChannel() {
        List<Map<String, List<?>>> attributes = new ArrayList<>();
        Map<String, List<?>> listAttr = new HashMap<>();
        listAttr.put("status", getStatusList());
        attributes.add(listAttr);

        return attributes;
    }


    // ------------------------------------------------------------------------------------------------

    // -- location --
    private List<AttributeResponse<Long>> getLocationList() {
        List<AttributeResponse<Long>> listStatusResponse = new ArrayList<>();
        listStatusResponse.add(new AttributeResponse<>(null, "All"));
        listStatusResponse.addAll(locationRepository.findAll().stream()
                .map((c) -> {
                    AttributeResponse<Long> response = new AttributeResponse<>();
                    response.setId(c.getId());
                    response.setName(c.getName());
                    return response;
                })
                .toList());

        return listStatusResponse;
    }

    // -- userTypes --
    private List<AttributeResponse<String>> getUserTypeList() {
        return Arrays.asList(
                new AttributeResponse<>(null, "All"),
                new AttributeResponse<>(UserType.MEMBER_SOLITAIRE.name(), "Solitaire"),
                new AttributeResponse<>(UserType.MEMBER_PRIORITY.name(), "Priority"),
                new AttributeResponse<>(UserType.NOT_MEMBER.name(), "Non Member")
        );
    }

    // -- status --
    private List<AttributeResponse<Boolean>> getStatusList() {
        return Arrays.asList(
                new AttributeResponse<>(null, "All"),
                new AttributeResponse<>(true, "Active"),
                new AttributeResponse<>(false, "Deactive")
        );
    }

    // -- role --
    private List<AttributeResponse<String>> getRoleList() {
        List<AttributeResponse<String>> responses = new ArrayList<>();
        responses.add(new AttributeResponse<>(null, "All"));
        responses.addAll(roleRepository.findAllByIdNotIn(Arrays.asList(1)).stream()
                .map((c) -> {
                    AttributeResponse<String> response = new AttributeResponse<>();
                    response.setId(c.getSecureId());
                    response.setName(c.getName());
                    return response;
                })
                .toList());
        return responses;
    }

    // -- seniority --
    private List<AttributeResponse<Boolean>> getSeniorityList() {
        return Arrays.asList(
                new AttributeResponse<>(null, "All"),
                new AttributeResponse<>(true, "Senior"),
                new AttributeResponse<>(false, "Youth")
        );
    }

}
