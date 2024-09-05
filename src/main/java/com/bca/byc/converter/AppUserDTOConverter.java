package com.bca.byc.converter;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.AppUserDetail;
import com.bca.byc.enums.UserType;
import com.bca.byc.model.AppRegisterRequest;
import com.bca.byc.model.UserInfoResponse;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.util.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

    public UserInfoResponse convertToInfoResponse(AppUser data) {
        // Mapping Entity with DTO Entity
        UserInfoResponse dto = modelMapper.map(data, UserInfoResponse.class);
        dto.setName(data.getName());
        dto.setEmail(data.getEmail());
        AppUserDetail appUserDetail = data.getAppUserDetail();
        dto.setStatus(appUserDetail.getStatus());
        dto.setType(appUserDetail.getType());
        dto.setAvatar(appUserDetail.getAvatar());
        dto.setCover(appUserDetail.getCover());
        dto.setBiodata(appUserDetail.getBiodata());
        dto.setCreatedAt(Formatter.formatDateTimeApps(appUserDetail.getCreatedAt()));

        // List of businesses
        if (data.getBusinesses() != null && !data.getBusinesses().isEmpty()) {
            List<UserInfoResponse.BusinessListResponse> businesses = data.getBusinesses().stream().map(b -> {
                UserInfoResponse.BusinessListResponse business = new UserInfoResponse.BusinessListResponse();
                business.setId(b.getId());
                business.setName(b.getName());
                business.setIsPrimary(b.getIsPrimary());

                List<UserInfoResponse.BusinessCategoryResponse> businessCategoryResponses = b.getBusinessCategories().stream()
                        .map(bc -> {
                            UserInfoResponse.BusinessCategoryResponse businessCategoryResponse = new UserInfoResponse.BusinessCategoryResponse();
                            businessCategoryResponse.setId(bc.getBusinessCategoryParent().getId());
                            businessCategoryResponse.setName(bc.getBusinessCategoryParent().getName());
                            return businessCategoryResponse;
                        }).collect(Collectors.toList());
//                 business.setCategory(businessCategoryResponses);

                // Set Line of Business based on the first category's parent name
                String lineOfBusiness = businessCategoryResponses.stream()
                        .map(UserInfoResponse.BusinessCategoryResponse::getName)
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
            UserInfoResponse.LocationListResponse locationResponse = new UserInfoResponse.LocationListResponse();
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

    // for get data
    public UserManagementDetailResponse convertToDetailResponse(AppUser data) {
        // mapping Entity with DTO Entity
        UserManagementDetailResponse dto = modelMapper.map(data, UserManagementDetailResponse.class);
        // Use DataFormatter here
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));

        // return
        return dto;
    }

    public UserManagementDetailResponse convertToListInquiry(AppUser data) {
        // mapping DTO Entity with Entity
        UserManagementDetailResponse dto = modelMapper.map(data, UserManagementDetailResponse.class);
        AppUserDetail userDetail = data.getAppUserDetail();

        dto.setName(data.getName());
        dto.setEmail(data.getEmail());
        dto.setPhone(userDetail.getPhone());

        String userType;
        LocalDate birthdate;
        if ( data.getAppUserDetail().getType() == UserType.MEMBER) {
            userType = "Parent Solitaire / Prioritas";
            birthdate = userDetail.getMemberBirthdate();
        } else if ( data.getAppUserDetail().getType() == UserType.NOT_MEMBER) {
            userType = "Child Solitaire / Prioritas";
            birthdate = userDetail.getChildBirthdate();
        } else {
            userType = "Not Member";
            birthdate = userDetail.getChildBirthdate();
        }

        dto.setBirthDate(Formatter.formatLocalDate(birthdate));

        dto.setType(userType);
        dto.setStatus(userDetail.getStatus());
        dto.setMemberCin(userDetail.getMemberCin());
        dto.setMemberBankAccount(userDetail.getMemberBankAccount());
        dto.setChildCin(userDetail.getChildCin());
        dto.setChildBankAccount(userDetail.getChildBankAccount());

        dto.setCreatedAt(Formatter.formatLocalDateTime(userDetail.getCreatedAt()));

        // return
        return dto;
    }
}
