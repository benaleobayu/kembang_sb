package com.bca.byc.service.impl;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.AppUserAttribute;
import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.enums.UserType;
import com.bca.byc.model.attribute.AttributeResponse;
import com.bca.byc.model.projection.CMSBulkDeleteProjection;
import com.bca.byc.repository.BusinessCategoryRepository;
import com.bca.byc.repository.LocationRepository;
import com.bca.byc.repository.RoleRepository;
import com.bca.byc.repository.UserManagementRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.service.GlobalAttributeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GlobalAttributeServiceIml implements GlobalAttributeService {

    private final AppAdminRepository adminRepository;
    private final UserManagementRepository userManagementRepository;

    private final LocationRepository locationRepository;
    private final BusinessCategoryRepository businessCategoryRepository;
    private final RoleRepository roleRepository;


    @Override
    public List<Map<String, List<?>>> listAttributePreRegister() {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);

        List<Map<String, List<?>>> attributes = new ArrayList<>();

        List<AttributeResponse<AdminApprovalStatus>> listStatusResponse = null;

        if (admin.getRole().getName().equals("SUPERADMIN")) {
            listStatusResponse = Arrays.asList(
                    new AttributeResponse<>(null, "All"),
                    new AttributeResponse<>(AdminApprovalStatus.PENDING, "Draft"),
                    new AttributeResponse<>(AdminApprovalStatus.OPT_APPROVED, "Waiting Approval"),
                    new AttributeResponse<>(AdminApprovalStatus.REJECTED, "Rejected"),
                    new AttributeResponse<>(AdminApprovalStatus.APPROVED, "Approved"),
                    new AttributeResponse<>(AdminApprovalStatus.DELETED, "Deleted")
            );
        }
        if (admin.getRole().getName().equals("ADMIN-OPERATIONAL")) {
            listStatusResponse = Arrays.asList(
                    new AttributeResponse<>(null, "All"),
                    new AttributeResponse<>(AdminApprovalStatus.OPT_APPROVED, "Waiting Approval"),
                    new AttributeResponse<>(AdminApprovalStatus.REJECTED, "Rejected"),
                    new AttributeResponse<>(AdminApprovalStatus.APPROVED, "Approved")
            );
        }
        if (admin.getRole().getName().equals("ADMIN-SUPERVISOR")) {
            listStatusResponse = Arrays.asList(
                    new AttributeResponse<>(null, "All"),
                    new AttributeResponse<>(AdminApprovalStatus.OPT_APPROVED, "Waiting Approval"),
                    new AttributeResponse<>(AdminApprovalStatus.REJECTED, "Rejected"),
                    new AttributeResponse<>(AdminApprovalStatus.APPROVED, "Approved")
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
        Map<String, List<?>> userTypeMap = new HashMap<>();
        userTypeMap.put("segmentation", getUserTypeList());
        attributes.add(userTypeMap);

        return attributes;
    }

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
    public List<Map<String, List<?>>> listAttributeSubBusinessCategory() {
        List<Map<String, List<?>>> attributes = new ArrayList<>();
        Map<String, List<?>> listStatus = new HashMap<>();
        listStatus.put("subCategory", getBusinessCategoryList());
        listStatus.put("segmentation", getUserTypeList());
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

    @Override
    public List<Map<String, List<?>>> listStatusTypeReportContentComment() {
        List<Map<String, List<?>>> attributes = new ArrayList<>();
        List<AttributeResponse<String>> listStatus = Arrays.asList(
                new AttributeResponse<>(null, "All"),
                new AttributeResponse<>("REQUEST", "Request"),
                new AttributeResponse<>("DRAFT", "Draft"),
                new AttributeResponse<>("REVIEW", "Review"),
                new AttributeResponse<>("REJECT", "Reject"),
                new AttributeResponse<>("TAKE_DOWN", "Take Down")
        );
        Map<String, List<?>> listAttr = new HashMap<>();
        listAttr.put("status", listStatus);
        attributes.add(listAttr);

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

    @Override
    public void makeUserBulkHardDeleteTrue(Set<String> ids) {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Set<CMSBulkDeleteProjection> userProjections = userManagementRepository.findToDeleteBySecureIdIn(ids);

        userProjections.forEach(projection -> {
            AppUser data = userManagementRepository.findById(projection.getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            AppUserAttribute userAttribute = data.getAppUserAttribute();
            userAttribute.setIsHardDeleted(true);
            data.setAppUserAttribute(userAttribute);
            data.setEmail(data.getEmail() + "_deleted");

            GlobalConverter.CmsAdminUpdateAtBy(data, admin);
            userManagementRepository.save(data);
        });
    }

    //----- helper -----

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

    // -- business category --
    private List<AttributeResponse<Long>> getBusinessCategoryList() {
        List<AttributeResponse<Long>> responses = new ArrayList<>();
        responses.add(new AttributeResponse<>(null, "All"));
        responses.addAll(businessCategoryRepository.findAll().stream()
                .map((c) -> {
                    AttributeResponse<Long> response = new AttributeResponse<>();
                    response.setId(c.getId());
                    response.setName(c.getName());
                    return response;
                })
                .toList());
        return responses;
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
