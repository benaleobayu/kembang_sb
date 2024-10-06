package com.bca.byc.converter.parsing;

import com.bca.byc.entity.Business;
import com.bca.byc.entity.ExpectItem;
import com.bca.byc.entity.UserHasExpect;
import com.bca.byc.model.apps.ExpectCategoryList;
import com.bca.byc.model.apps.SubExpectCategoryList;
import com.bca.byc.model.data.BusinessListResponse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TreeUserResponse {

    public static List<BusinessListResponse> convertListBusinesses(List<Business> businesses) {
        return businesses.stream().map(business -> {
            BusinessListResponse businessResponse = new BusinessListResponse();
            businessResponse.setId(business.getSecureId());
            businessResponse.setIndex(business.getId());
            businessResponse.setName(business.getName());
            businessResponse.setAddress(business.getAddress());
            businessResponse.setLineOfBusiness(business.getBusinessCategories().stream().findFirst().get().getBusinessCategoryParent().getName());
            businessResponse.setIsPrimary(business.getIsPrimary());
            businessResponse.setTotalCatalogs(!business.getBusinessCatalogs().isEmpty() ? business.getBusinessCatalogs().size() : 0);

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
        }).collect(Collectors.toList());
    }

    public static BusinessListResponse convertSingleBusinesses(Business data, BusinessListResponse dto) {
            dto.setId(data.getSecureId());
            dto.setIndex(data.getId());
            dto.setName(data.getName());
            dto.setAddress(data.getAddress());
            dto.setLineOfBusiness(data.getBusinessCategories().stream().findFirst().get().getBusinessCategoryParent().getName());
            dto.setIsPrimary(data.getIsPrimary());
            dto.setTotalCatalogs(!data.getBusinessCatalogs().isEmpty() ? data.getBusinessCatalogs().size() : 0);

            List<BusinessListResponse.LocationListResponse> locationListResponses = data.getBusinessHasLocations().stream()
                    .map(bhl -> {
                        BusinessListResponse.LocationListResponse locationListResponse = new BusinessListResponse.LocationListResponse();
                        locationListResponse.setId(bhl.getLocation().getSecureId());
                        locationListResponse.setIndex(bhl.getLocation().getId());
                        locationListResponse.setName(bhl.getLocation().getName());
                        return locationListResponse;
                    })
                    .collect(Collectors.toList());
            dto.setLocations(locationListResponses);

            List<BusinessListResponse.BusinessCategoryResponse> businessCategoryResponses = data.getBusinessCategories().stream()
                    .map(bc -> {
                        BusinessListResponse.BusinessCategoryResponse businessCategoryResponse = new BusinessListResponse.BusinessCategoryResponse();
                        businessCategoryResponse.setId(bc.getBusinessCategoryChild().getSecureId());
                        businessCategoryResponse.setIndex(bc.getBusinessCategoryChild().getId());
                        businessCategoryResponse.setName(bc.getBusinessCategoryChild().getName());
                        return businessCategoryResponse;
                    }).collect(Collectors.toList());
            dto.setSubCategories(businessCategoryResponses);

            return dto;
    }

    public static List<ExpectCategoryList> convertExpectCategories(List<UserHasExpect> userHasExpects) {
        return userHasExpects.stream()
                .filter(ue -> ue.getExpectItem() != null)
                .map(ue -> {
                    ExpectCategoryList expectCategoryDetailResponse = new ExpectCategoryList();
                    expectCategoryDetailResponse.setId(ue.getExpectCategory().getSecureId());
                    expectCategoryDetailResponse.setIndex(ue.getExpectCategory().getOrders());
                    expectCategoryDetailResponse.setName(ue.getExpectCategory().getName());

                    List<ExpectItem> expectItems = ue.getExpectCategory().getExpectItems().stream()
                            .sorted(Comparator.comparing(ExpectItem::getOrders))
                            .limit(3)
                            .collect(Collectors.toList());

                    expectCategoryDetailResponse.setSubCategories(expectItems.stream()
                            .filter(ei -> ei.getId().equals(ue.getExpectItem().getId()))
                            .map(ei -> {
                                SubExpectCategoryList expectItem = new SubExpectCategoryList();
                                expectItem.setId(ei.getId());
                                expectItem.setName(ei.getName());
                                return expectItem;
                            })
                            .collect(Collectors.toList()));

                    return expectCategoryDetailResponse;
                }).collect(Collectors.toList());
    }
}
