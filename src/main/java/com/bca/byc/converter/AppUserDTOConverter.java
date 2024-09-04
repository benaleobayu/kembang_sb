package com.bca.byc.converter;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.AppUserDetail;
import com.bca.byc.model.ApiUserInfoDetailResponse;
import com.bca.byc.model.AppRegisterRequest;
import com.bca.byc.util.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class AppUserDTOConverter {
    private ModelMapper modelMapper;

    // for create data
    public AppUser convertToCreateRequest(@Valid AppRegisterRequest dto) {
        // mapping DTO Entity with Entity
        AppUser data = modelMapper.map(dto, AppUser.class);
        AppUserDetail userAttributes = new AppUserDetail();

        data.setAppUserDetail(userAttributes);
        // return
        return data;
    }

    public ApiUserInfoDetailResponse convertToInfoResponse(AppUser data) {
        // Mapping Entity with DTO Entity
        ApiUserInfoDetailResponse dto = modelMapper.map(data, ApiUserInfoDetailResponse.class);
        dto.setName(data.getName());
        dto.setEmail(data.getEmail());
        AppUserDetail appUserDetail = data.getAppUserDetail();
        dto.setStatus(appUserDetail.getStatus());
        dto.setType(appUserDetail.getType());
        dto.setAvatar(appUserDetail.getAvatar());
        dto.setCover(appUserDetail.getCover());
        dto.setBiodata(appUserDetail.getBiodata());
        dto.setCreatedAt(Formatter.formatLocalDateTime(appUserDetail.getCreatedAt()));

        // List of businesses
        if (data.getBusinesses() != null && !data.getBusinesses().isEmpty()) {
            List<ApiUserInfoDetailResponse.BusinessListResponse> businesses = data.getBusinesses().stream().map(b -> {
                ApiUserInfoDetailResponse.BusinessListResponse business = new ApiUserInfoDetailResponse.BusinessListResponse();
                business.setId(b.getId());
                business.setName(b.getName());
                business.setIsPrimary(b.getIsPrimary());

                List<ApiUserInfoDetailResponse.BusinessCategoryResponse> businessCategoryResponses = b.getBusinessCategories().stream()
                        .map(bc -> {
                            ApiUserInfoDetailResponse.BusinessCategoryResponse businessCategoryResponse = new ApiUserInfoDetailResponse.BusinessCategoryResponse();
                            businessCategoryResponse.setId(bc.getBusinessCategoryParent().getId());
                            businessCategoryResponse.setName(bc.getBusinessCategoryParent().getName());
                            return businessCategoryResponse;
                        }).collect(Collectors.toList());
//                 business.setCategory(businessCategoryResponses);

                // Set Line of Business based on the first category's parent name
                String lineOfBusiness = businessCategoryResponses.stream()
                        .map(ApiUserInfoDetailResponse.BusinessCategoryResponse::getName)
                        .findFirst()
                        .orElse("Unknown Business Category");
                business.setLineOfBusiness(lineOfBusiness);

                return business;
            }).collect(Collectors.toList());
            dto.setBusinesses(businesses);
        } else {
            log.warn("Businesses list is empty or null for user: {}", data.getId());
        }

        // Other attributes
        if (data.getLocation() != null) {
            ApiUserInfoDetailResponse.LocationListResponse locationResponse = new ApiUserInfoDetailResponse.LocationListResponse();
            locationResponse.setId(data.getLocation().getId());
            locationResponse.setName(data.getLocation().getName());
            dto.setLocation(locationResponse);
        } else {
            log.warn("Location is null for user: {}", data.getId());
        }

        // Count followers, following, posts, events
        dto.setTotalFollowers(data.getFollowers() != null ? data.getFollowers().size() : 0);
        dto.setTotalFollowing(data.getFollows() != null ? data.getFollows().size() : 0);
        dto.setTotalPosts(0);  // Placeholder, update as needed
        dto.setTotalEvents(0);  // Placeholder, update as needed

        return dto;
    }

}
