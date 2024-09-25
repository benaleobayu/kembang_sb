package com.bca.byc.converter;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Business;
import com.bca.byc.enums.StatusType;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.apps.OwnerDataResponse;
import com.bca.byc.model.apps.PostContentDetailResponse;
import com.bca.byc.model.apps.PostOwnerResponse;
import com.bca.byc.model.data.BusinessListResponse;
import com.bca.byc.util.helper.Formatter;

import java.util.List;
import java.util.stream.Collectors;

public class UserManagementConverter {

    private final String baseUrl;

    public UserManagementConverter(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void DetailResponse(
            AppUser data,
            UserManagementDetailResponse dto
    ) {
        dto.setName(data.getAppUserDetail().getName());
        dto.setBirthDate(data.getAppUserDetail().getUserAs() == null || data.getAppUserDetail().getUserAs().equalsIgnoreCase("member") ?
                Formatter.formatLocalDate(data.getAppUserDetail().getMemberBirthdate()) :
                Formatter.formatLocalDate(data.getAppUserDetail().getParentBirthdate()));
        dto.setEmail(data.getEmail().toLowerCase());
        dto.setPhone(data.getAppUserDetail().getPhone());
        dto.setMemberCin(data.getAppUserDetail().getMemberCin());
        dto.setMemberCardNumber(data.getAppUserDetail().getMemberBankAccount());
        dto.setType(data.getAppUserDetail().getType().toString());
        dto.setMemberType(data.getAppUserDetail().getMemberType());
        dto.setParentCin(data.getAppUserDetail().getParentCin());
        dto.setParentBankAccount(data.getAppUserDetail().getParentBankAccount());
        dto.setBranchCode(data.getAppUserDetail().getBranchCode());
        dto.setPicName(data.getAppUserDetail().getPicName());

        List<Business> businesses = data.getBusinesses();
        dto.setBusinesses(businesses.stream().map(business -> {
            BusinessListResponse businessResponse = new BusinessListResponse();
            businessResponse.setId(business.getId());
            businessResponse.setName(business.getName());
            businessResponse.setAddress(business.getAddress());
            businessResponse.setLineOfBusiness(business.getBusinessCategories().stream().findFirst().get().getBusinessCategoryParent().getName());
            businessResponse.setIsPrimary(business.getIsPrimary());

            List<BusinessListResponse.LocationListResponse> locationListResponses = business.getBusinessHasLocations().stream()
                    .map(bhl -> {
                        BusinessListResponse.LocationListResponse locationListResponse = new BusinessListResponse.LocationListResponse();
                        locationListResponse.setId(bhl.getLocation().getId());
                        locationListResponse.setName(bhl.getLocation().getName());
                        return locationListResponse;
                    })
                    .collect(Collectors.toList());
            businessResponse.setLocations(locationListResponses);

            List<BusinessListResponse.BusinessCategoryResponse> businessCategoryResponses = business.getBusinessCategories().stream()
                    .map(bc -> {
                        BusinessListResponse.BusinessCategoryResponse businessCategoryResponse = new BusinessListResponse.BusinessCategoryResponse();
                        businessCategoryResponse.setId(bc.getBusinessCategoryChild().getId());
                        businessCategoryResponse.setName(bc.getBusinessCategoryChild().getName());
                        return businessCategoryResponse;
                    }).collect(Collectors.toList());
            businessResponse.setSubCategories(businessCategoryResponses);

            return businessResponse;
        }).collect(Collectors.toList()));

        List<UserManagementDetailResponse.ExpectCategoryList> expectCategories = data.getUserHasExpects().stream()
                .filter(ue -> ue.getExpectItem() != null)
                .map(ue -> {
                    UserManagementDetailResponse.ExpectCategoryList expectCategoryDetailResponse = new UserManagementDetailResponse.ExpectCategoryList();
                    expectCategoryDetailResponse.setId(ue.getExpectCategory().getId());
                    expectCategoryDetailResponse.setName(ue.getExpectCategory().getName());
                    expectCategoryDetailResponse.setSubCategories(ue.getExpectCategory().getExpectItems().stream()
                            .filter(ei -> ei.getId().equals(ue.getExpectItem().getId()))
                            .map(ei -> {
                                        UserManagementDetailResponse.SubExpectCategoryList expectItem = new UserManagementDetailResponse.SubExpectCategoryList();
                                        expectItem.setId(ei.getId());
                                        expectItem.setName(ei.getName());
                                        return expectItem;
                                    }
                            ).collect(Collectors.toList()));
                    return expectCategoryDetailResponse;
                }).collect(Collectors.toList());

        dto.setExpectCategory(expectCategories);

        dto.setOrders(data.getAppUserDetail().getId());
        dto.setStatus(
                data.getAppUserDetail().getStatus().equals(StatusType.ACTIVATED)
                        ? data.getAppUserAttribute().getIsSuspended()
                        ? data.getAppUserAttribute().getIsDeleted()
                        ? "Deleted User"
                        : "Suspended User"
                        : "Active User"
                        : "Inactive User"
        );
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getAppUserDetail().getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getAppUserDetail().getUpdatedAt()));
    }


    public PostOwnerResponse PostOwnerResponse(
            PostOwnerResponse dto,
            Long id,
            String name,
            String avatar,
            String businessName,
            String lineOfBusiness,
            Boolean isPrimary
    ) {
        dto.setId(id);
        dto.setName(name);
        dto.setAvatar(avatar != null && avatar.startsWith("uploads/") ? baseUrl + "/" + avatar : avatar);
        dto.setBusinessName(businessName);
        dto.setLineOfBusiness(lineOfBusiness);
        dto.setIsPrimary(isPrimary);

        return dto;
    }

    public PostContentDetailResponse PostContentDetailResponse(
            PostContentDetailResponse dto,
            Long contentId,
            String content,
            String contentType,
            String thumbnail,
            List<OwnerDataResponse> tagsUser
    ) {
        dto.setContentId(contentId);
        dto.setContent(content != null && content.startsWith("uploads/") ? baseUrl + "/" + content : content);
        dto.setContentType(contentType);
        dto.setThumbnail(thumbnail);
        dto.setContentTagsUser(tagsUser);
        return dto;
    }

    public OwnerDataResponse OwnerDataResponse(
            OwnerDataResponse dto,
            Long id,
            String name,
            String avatar
    ) {
        dto.setId(id);
        dto.setName(name);
        dto.setAvatar(avatar != null && avatar.startsWith("uploads/") ? baseUrl + "/" + avatar : avatar);
        return dto;
    }
}
