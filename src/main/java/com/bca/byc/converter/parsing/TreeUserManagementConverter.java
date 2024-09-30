package com.bca.byc.converter.parsing;

import com.bca.byc.entity.*;
import com.bca.byc.enums.StatusType;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.apps.*;
import com.bca.byc.model.data.BusinessListResponse;
import com.bca.byc.util.helper.Formatter;

import java.util.List;
import java.util.stream.Collectors;

public class TreeUserManagementConverter {

    public void DetailResponse(
            AppUser data,
            UserManagementDetailResponse dto
    ) {
        dto.setId(data.getSecureId());
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
        dto.setOrders(data.getAppUserDetail().getOrders());
        dto.setBranchCode(data.getAppUserDetail().getBranchCode() != null ? data.getAppUserDetail().getBranchCode().getCode() : null);
        dto.setPicName(data.getAppUserDetail().getPicName());

        List<Business> businesses = data.getBusinesses();
        dto.setBusinesses(businesses.stream().map(business -> {
            BusinessListResponse businessResponse = new BusinessListResponse();
            businessResponse.setId(business.getSecureId());
            businessResponse.setName(business.getName());
            businessResponse.setAddress(business.getAddress());
            businessResponse.setLineOfBusiness(business.getBusinessCategories().stream().findFirst().get().getBusinessCategoryParent().getName());
            businessResponse.setIsPrimary(business.getIsPrimary());

            List<BusinessListResponse.LocationListResponse> locationListResponses = business.getBusinessHasLocations().stream()
                    .map(bhl -> {
                        BusinessListResponse.LocationListResponse locationListResponse = new BusinessListResponse.LocationListResponse();
                        locationListResponse.setId(bhl.getLocation().getSecureId());
                        locationListResponse.setIndex(bhl.getLocation().getId());
                        locationListResponse.setName(bhl.getLocation().getName());
                        return locationListResponse;
                    })
                    .collect(Collectors.toList());
            businessResponse.setLocations(locationListResponses);

            List<BusinessListResponse.BusinessCategoryResponse> businessCategoryResponses = business.getBusinessCategories().stream()
                    .map(bc -> {
                        BusinessListResponse.BusinessCategoryResponse businessCategoryResponse = new BusinessListResponse.BusinessCategoryResponse();
                        businessCategoryResponse.setId(bc.getBusinessCategoryChild().getSecureId());
                        businessCategoryResponse.setIndex(bc.getBusinessCategoryChild().getId());
                        businessCategoryResponse.setName(bc.getBusinessCategoryChild().getName());
                        return businessCategoryResponse;
                    }).collect(Collectors.toList());
            businessResponse.setSubCategories(businessCategoryResponses);

            return businessResponse;
        }).collect(Collectors.toList()));

        List<ExpectCategoryList> expectCategories = data.getUserHasExpects().stream()
                .filter(ue -> ue.getExpectItem() != null)
                .map(ue -> {
                    ExpectCategoryList expectCategoryDetailResponse = new ExpectCategoryList();
                    expectCategoryDetailResponse.setId(ue.getExpectCategory().getId());
                    expectCategoryDetailResponse.setName(ue.getExpectCategory().getName());
                    expectCategoryDetailResponse.setSubCategories(ue.getExpectCategory().getExpectItems().stream()
                            .filter(ei -> ei.getId().equals(ue.getExpectItem().getId()))
                            .map(ei -> {
                                        SubExpectCategoryList expectItem = new SubExpectCategoryList();
                                        expectItem.setId(ei.getId());
                                        expectItem.setName(ei.getName());
                                        return expectItem;
                                    }
                            ).collect(Collectors.toList()));
                    return expectCategoryDetailResponse;
                }).collect(Collectors.toList());

        dto.setExpectCategory(expectCategories);

        dto.setOrders(data.getAppUserDetail().getOrders());
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
}

