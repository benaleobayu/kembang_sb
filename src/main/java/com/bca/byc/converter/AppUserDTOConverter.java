package com.bca.byc.converter;

import com.bca.byc.entity.*;
import com.bca.byc.enums.UserType;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AppRegisterRequest;
import com.bca.byc.model.AppUserProfileRequest;
import com.bca.byc.model.UserInfoResponse;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.repository.ExpectCategoryRepository;
import com.bca.byc.repository.ExpectItemRepository;
import com.bca.byc.repository.LocationRepository;
import com.bca.byc.repository.UserHasExpectRepository;
import com.bca.byc.util.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class AppUserDTOConverter {
    private final ExpectCategoryRepository expectCategoryRepository;
    private final ExpectItemRepository expectItemRepository;
    private final UserHasExpectRepository userHasExpectRepository;
    private final LocationRepository locationRepository;
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
        if (data.getAppUserDetail().getType() == UserType.MEMBER) {
            userType = "Parent Solitaire / Prioritas";
            birthdate = userDetail.getMemberBirthdate();
        } else if (data.getAppUserDetail().getType() == UserType.NOT_MEMBER) {
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


    public void convertToUpdateProfile(AppUser data, AppUserProfileRequest dto) {
        modelMapper.map(dto, data);
        data.setName(dto.getName());
        data.setEmail(dto.getEmail());

        Location location = locationRepository.findById(dto.getLocation())
                .orElseThrow(() -> new BadRequestException("Location not found"));

        data.setLocation(location);

        AppUserDetail appUserDetail = data.getAppUserDetail();
        appUserDetail.setPhone(dto.getPhone());
        appUserDetail.setBiodata(dto.getBiography());

        List<String> education = dto.getEducation();
        if (education != null) {
            appUserDetail.setEducation(String.join(", ", education));
        } else {
            appUserDetail.setEducation(null);
        }

        for (AppUserProfileRequest.ProfileExpectCategoryResponse expectDto : dto.getUserHasExpects()) {
            ExpectCategory expectCategory = expectCategoryRepository.findById(expectDto.getExpectCategoryId())
                    .orElseThrow(() -> new BadRequestException("Expect Category not found"));

            if (expectDto.getItems() != null && expectDto.getItems().getIds() != null && !expectDto.getItems().getIds().isEmpty()) {
                for (Long expectItemId : expectDto.getItems().getIds()) {
                    ExpectItem expectItem = expectItemRepository.findById(expectItemId)
                            .orElseThrow(() -> new BadRequestException("Expect Item not found"));

                    UserHasExpectId userHasExpectId = new UserHasExpectId();
                    userHasExpectId.setUserId(data.getId());
                    userHasExpectId.setExpectCategoryId(expectCategory.getId());
                    userHasExpectId.setExpectItemId(expectItem.getId());

                    UserHasExpect userHasExpect = new UserHasExpect();
                    userHasExpect.setId(userHasExpectId);
                    userHasExpect.setUser(data);
                    userHasExpect.setExpectCategory(expectCategory);
                    userHasExpect.setExpectItem(expectItem);
                    userHasExpect.setOtherExpect(expectDto.getOtherExpect());
                    userHasExpect.setOtherExpectItem(expectDto.getItems().getOtherExpectItem());

                    userHasExpectRepository.save(userHasExpect);
                }
            } else if (expectDto.getExpectCategoryId() == 5) {
                UserHasExpect userHasExpect = new UserHasExpect();
                ExpectItem expectItem = expectItemRepository.findById(5L)
                        .orElseThrow(() -> new BadRequestException("Expect Item not found"));
                userHasExpect.setUser(data);
                userHasExpect.setExpectCategory(expectCategory);
                userHasExpect.setExpectItem(expectItem);
                userHasExpect.setOtherExpect(expectDto.getOtherExpect());
                userHasExpectRepository.save(userHasExpect);
            }
        }


    }
}
