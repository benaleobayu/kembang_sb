package com.bca.byc.service.impl;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.*;
import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.enums.AdminType;
import com.bca.byc.enums.UserType;
import com.bca.byc.model.attribute.AttributeResponse;
import com.bca.byc.model.projection.CMSBulkDeleteProjection;
import com.bca.byc.repository.BusinessCategoryRepository;
import com.bca.byc.repository.LocationRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.service.UserManagementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private final AppAdminRepository adminRepository;
    private final UserManagementRepository userManagementRepository;

    private final LocationRepository locationRepository;
    private final BusinessCategoryRepository businessCategoryRepository;


    @Override
    public List<Map<String, List<?>>> listAttributePreRegister() {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);

        List<Map<String, List<?>>> attributes = new ArrayList<>();

        List<AttributeResponse<AdminApprovalStatus>> listStatusResponse = null;

        if (admin.getType().equals(AdminType.SUPERADMIN)) {
            listStatusResponse = Arrays.asList(
                    new AttributeResponse<>( null, "All"),
                    new AttributeResponse<>( AdminApprovalStatus.PENDING, "Draft"),
                    new AttributeResponse<>( AdminApprovalStatus.OPT_APPROVED,  "Waiting Approval"),
                    new AttributeResponse<>( AdminApprovalStatus.REJECTED, "Rejected"),
                    new AttributeResponse<>( AdminApprovalStatus.APPROVED, "Approved"),
                    new AttributeResponse<>( AdminApprovalStatus.DELETED, "Deleted")
            );
        }
        if (admin.getType().equals(AdminType.OPERATIONAL)) {
            listStatusResponse = Arrays.asList(
                    new AttributeResponse<>( null, "All"),
                    new AttributeResponse<>( AdminApprovalStatus.OPT_APPROVED,  "Waiting Approval"),
                    new AttributeResponse<>( AdminApprovalStatus.REJECTED, "Rejected"),
                    new AttributeResponse<>( AdminApprovalStatus.APPROVED, "Approved")
            );
        }
        if (admin.getType().equals(AdminType.SUPERVISOR)) {
            listStatusResponse = Arrays.asList(
                    new AttributeResponse<>( null, "All"),
                    new AttributeResponse<>( AdminApprovalStatus.OPT_APPROVED,  "Waiting Approval"),
                    new AttributeResponse<>( AdminApprovalStatus.REJECTED, "Rejected"),
                    new AttributeResponse<>( AdminApprovalStatus.APPROVED, "Approved")
            );
        }

        Map<String, List<?>> listStatus = new HashMap<>();
        listStatus.put("status", listStatusResponse);
        attributes.add(listStatus);

        return attributes;
    }

    @Override
    public List<Map<String, List<?>>> listAttributeCreateUpdatePreRegister() {
        List<Map<String, List<?>>> attributes = new ArrayList<>();
        // Data UserTypes
        List<AttributeResponse<Integer>> listUserTypes = Arrays.asList(
                new AttributeResponse<>( UserType.MEMBER_SOLITAIRE.ordinal(), "Solitaire"),
                new AttributeResponse<>( UserType.MEMBER_PRIORITY.ordinal(),  "Priority"),
                new AttributeResponse<>( UserType.NOT_MEMBER.ordinal(), "Non Member")
        );
        Map<String, List<?>> userTypeMap = new HashMap<>();
        userTypeMap.put("segmentation", listUserTypes);
        attributes.add(userTypeMap);

        return attributes;
    }

    @Override
    public List<Map<String, List<?>>> listAttributeUserManagement() {
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

    @Override
    public List<Map<String, List<?>>> listAttributeSubBusinessCategory() {
        List<BusinessCategory> subCategory = new ArrayList<>();
        for (BusinessCategory category : businessCategoryRepository.findAll()) {
            BusinessCategory businessCategory = new BusinessCategory();
            businessCategory.setId(category.getId());
            businessCategory.setName(category.getName());
            subCategory.add(businessCategory);
        }

        List<AttributeResponse> listSubCategory = subCategory.stream()
                .map((c) -> {
                    AttributeResponse<Long> response = new AttributeResponse<>();
                    response.setId(c.getId());
                    response.setName(c.getName());
                    return response;
                })
                .collect(Collectors.toList());

        List<AttributeResponse<Integer>> listUserTypes = Arrays.asList(
                new AttributeResponse<>( UserType.MEMBER_SOLITAIRE.ordinal(), "Solitaire"),
                new AttributeResponse<>( UserType.MEMBER_PRIORITY.ordinal(),  "Priority"),
                new AttributeResponse<>( UserType.NOT_MEMBER.ordinal(), "Non Member")
        );

        List<Map<String, List<?>>> attributes = new ArrayList<>();

        Map<String, List<?>> listStatus = new HashMap<>();
        listStatus.put("subCategory", listSubCategory);
        listStatus.put("segmentation", listUserTypes);
        attributes.add(listStatus);

        return attributes;
    }

    // ------------------------------------------------------------------------------------------------

    @Override
    public void makeUserBulkDeleteTrue(Set<String> ids) {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Set<CMSBulkDeleteProjection> userProjections = userManagementRepository.findToDeleteBySecureIdIn(ids);

        userProjections.forEach(projection -> {
            AppUser data = userManagementRepository.findById(projection.getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            AppUserAttribute userAttribute = data.getAppUserAttribute();
            userAttribute.setIsDeleted(true);
            data.setAppUserAttribute(userAttribute);

            GlobalConverter.CmsAdminUpdateAtBy(data, admin);
            userManagementRepository.save(data);
        });
    }
}
