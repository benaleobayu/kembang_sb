package com.bca.byc.converter.parsing;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.Business;
import com.bca.byc.entity.ExpectItem;
import com.bca.byc.entity.UserHasExpect;
import com.bca.byc.model.*;
import com.bca.byc.model.apps.ExpectCategoryList;
import com.bca.byc.model.apps.SubExpectCategoryList;
import com.bca.byc.model.data.BusinessListResponse;

import java.util.*;
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

            List<LocationListResponse> locationListResponses = business.getBusinessHasLocations().stream()
                    .map(bhl -> {
                        LocationListResponse locationListResponse = new LocationListResponse();
                        locationListResponse.setId(bhl.getLocation().getSecureId());
                        locationListResponse.setIndex(bhl.getLocation().getId());
                        locationListResponse.setName(bhl.getLocation().getName());
                        return locationListResponse;
                    })
                    .collect(Collectors.toList());
            businessResponse.setLocations(locationListResponses);

            List<BusinessCategoryResponse> businessCategoryResponses = business.getBusinessCategories().stream()
                    .map(bc -> {
                        BusinessCategoryResponse businessCategoryResponse = new BusinessCategoryResponse();
                        businessCategoryResponse.setId(bc.getBusinessCategoryChild().getSecureId());
                        businessCategoryResponse.setIndex(bc.getBusinessCategoryChild().getId());
                        businessCategoryResponse.setName(bc.getBusinessCategoryChild().getName());
                        return businessCategoryResponse;
                    }).collect(Collectors.toList());
            businessResponse.setSubCategories(businessCategoryResponses);

            return businessResponse;
        }).collect(Collectors.toList());
    }

    public static BusinessDetailResponse convertSingleBusinesses(Business data, BusinessDetailResponse dto) {
        dto.setId(data.getSecureId());
        dto.setIndex(data.getId());
        dto.setName(data.getName());
        dto.setAddress(data.getAddress());
        dto.setTotalCatalogs(data.getBusinessCatalogs().size());
        dto.setIsPrimary(data.getIsPrimary());

        // Mapping locations
        List<LocationListResponse> locationListResponses = data.getBusinessHasLocations().stream()
                .map(bhl -> {
                    LocationListResponse locationListResponse = new LocationListResponse();
                    locationListResponse.setId(bhl.getLocation().getSecureId());
                    locationListResponse.setIndex(bhl.getLocation().getId());
                    locationListResponse.setName(bhl.getLocation().getName());
                    return locationListResponse;
                })
                .collect(Collectors.toList());
        dto.setLocations(locationListResponses);

        // Mapping categories
        Map<String, BusinessCategoryParentResponse> parentMap = new HashMap<>();
        data.getBusinessCategories().forEach(bc -> {
            String parentId = bc.getBusinessCategoryParent().getSecureId();
            if (!parentMap.containsKey(parentId)) {
                BusinessCategoryParentResponse parentResponse = new BusinessCategoryParentResponse();
                parentResponse.setId(parentId);
                parentResponse.setIndex(bc.getBusinessCategoryParent().getId());
                parentResponse.setName(bc.getBusinessCategoryParent().getName());
                parentResponse.setSubCategories(new ArrayList<>());
                parentMap.put(parentId, parentResponse);
            }
            BusinessCategoryResponse childResponse = new BusinessCategoryResponse();
            childResponse.setId(bc.getBusinessCategoryChild().getSecureId());
            childResponse.setIndex(bc.getBusinessCategoryChild().getId());
            childResponse.setName(bc.getBusinessCategoryChild().getName());

            parentMap.get(parentId).getSubCategories().add(childResponse);
        });

        // Assuming we want the first category parent response
        dto.setCategory(parentMap.values().stream().findFirst().orElse(null));

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

                    List<ExpectItem> expectItems = ue.getExpectCategory().getExpectItems();

                    // Check if expectItems is not null before sorting
                    if (expectItems != null) {
                        expectItems = expectItems.stream()
                                .filter(Objects::nonNull) // Filter out null ExpectItem
                                .sorted(Comparator.comparing(ExpectItem::getOrders, Comparator.nullsLast(Comparator.naturalOrder())))
                                .limit(3)
                                .collect(Collectors.toList());
                    }

                    // Setting subCategories
                    expectCategoryDetailResponse.setSubCategories(expectItems == null ? new ArrayList<>() : expectItems.stream()
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


    public static SuggestedUserResponse convertToCardUser(AppUser user, String baseUrl) {
        SuggestedUserResponse response = new SuggestedUserResponse();
        response.setUserId(user.getSecureId());
        response.setUserAvatar(GlobalConverter.getParseImage(user.getAppUserDetail().getAvatar(), baseUrl));
        response.setUserName(user.getAppUserDetail().getName());
        response.setBusinessName(user.getBusinesses().stream()
                .filter(Business::getIsPrimary)
                .map(Business::getName)
                .findFirst().orElse(null));
        response.setBusinessLob(user.getBusinesses().stream()
                .filter(Business::getIsPrimary)
                .flatMap(b-> b.getBusinessCategories().stream()
                        .map(bc -> bc.getBusinessCategoryParent().getName()))
                .findFirst().orElse(null));
        boolean isFollowed = user.getFollowers().stream().anyMatch(f -> f.getId().equals(user.getId()));
        response.setIsFollowed(isFollowed);
        return response;
    }
}
