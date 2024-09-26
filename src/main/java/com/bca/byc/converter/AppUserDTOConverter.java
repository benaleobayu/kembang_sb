package com.bca.byc.converter;

import com.bca.byc.entity.*;
import com.bca.byc.enums.UserType;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AppRegisterRequest;
import com.bca.byc.model.AppUserProfileRequest;
import com.bca.byc.model.UserInfoResponse;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.apps.ExpectCategoryList;
import com.bca.byc.model.apps.SubExpectCategoryList;
import com.bca.byc.model.data.BusinessListResponse;
import com.bca.byc.repository.ExpectCategoryRepository;
import com.bca.byc.repository.ExpectItemRepository;
import com.bca.byc.repository.LocationRepository;
import com.bca.byc.repository.UserHasExpectRepository;
import com.bca.byc.util.helper.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class AppUserDTOConverter {

    private final ExpectCategoryRepository expectCategoryRepository;
    private final ExpectItemRepository expectItemRepository;
    private final UserHasExpectRepository userHasExpectRepository;
    private final LocationRepository locationRepository;
    @Value("${app.base.url}")
    private String baseUrl;
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
        dto.setAvatar(appUserDetail.getAvatar() != null && appUserDetail.getAvatar().startsWith("uploads/") ? baseUrl + "/" + appUserDetail.getAvatar() : appUserDetail.getAvatar());
        dto.setCover(appUserDetail.getCover() != null && appUserDetail.getCover().startsWith("uploads/") ? baseUrl + "/" + appUserDetail.getCover() : appUserDetail.getCover());
        dto.setBiodata(appUserDetail.getBiodata());
        dto.setCountryCode(appUserDetail.getCountryCode());
        dto.setPhone(appUserDetail.getPhone());
        dto.setEducation(appUserDetail.getEducation());
        dto.setCreatedAt(Formatter.formatDateTimeApps(appUserDetail.getCreatedAt()));

        // List of businesses
        if (data.getBusinesses() != null && !data.getBusinesses().isEmpty()) {
            List<BusinessListResponse> businesses = data.getBusinesses().stream().map(b -> {
                BusinessListResponse business = new BusinessListResponse();
                business.setId(b.getId());
                business.setName(b.getName());
                business.setIsPrimary(b.getIsPrimary());

                List<BusinessListResponse.BusinessCategoryResponse> businessCategoryResponses = b.getBusinessCategories().stream()
                        .map(bc -> {
                            BusinessListResponse.BusinessCategoryResponse businessCategoryResponse = new BusinessListResponse.BusinessCategoryResponse();
                            businessCategoryResponse.setId(bc.getBusinessCategoryParent().getId());
                            businessCategoryResponse.setName(bc.getBusinessCategoryParent().getName());
                            return businessCategoryResponse;
                        }).collect(Collectors.toList());
//                 business.setCategory(businessCategoryResponses);

                // Set Line of Business based on the first category's parent name
                String lineOfBusiness = businessCategoryResponses.stream()
                        .map(BusinessListResponse.BusinessCategoryResponse::getName)
                        .findFirst()
                        .orElse("Unknown Business Category");
                business.setLineOfBusiness(lineOfBusiness);

                return business;
            }).collect(Collectors.toList());
            dto.setBusinesses(businesses);
        } else {
            log.warn("Businesses list is empty or null for user: {}", data.getId());
        }
        // List of expect categories
        Map<Long, ExpectCategoryList> expectCategoryListMap = new HashMap<>();

        for (UserHasExpect ec : data.getUserHasExpects()) {
            Long categoryId = ec.getExpectCategory().getId();
            String categoryName = ec.getExpectCategory().getName();

            // check if the category already exists in the map
            ExpectCategoryList ecList = expectCategoryListMap.get(categoryId);
            if (ecList == null) {
                ecList = new ExpectCategoryList();
                ecList.setId(categoryId);
                ecList.setName(categoryName);
                expectCategoryListMap.put(categoryId, ecList);

                if (categoryName.equals("Other")) {
                    List<String> otherValue = new ArrayList<>();
                    otherValue.add(ec.getOtherExpect());
                    ecList.setOtherValue(otherValue);
                    ecList.setSubCategories(new ArrayList<>());

                } else {
                    ecList.setOtherValue(null);
                }
                expectCategoryListMap.put(categoryId, ecList);
            }

            // add sub-category if present
            for (UserHasExpect subEc : ec.getExpectItem().getUserHasExpects()) {
                Long subCategoryId = subEc.getExpectItem().getId();
                String subCategoryName = subEc.getExpectItem().getName();

                SubExpectCategoryList subEcList = new SubExpectCategoryList();
                subEcList.setId(subCategoryId);
                subEcList.setName(subCategoryName);

                if (subCategoryName.equals("Other")) {
                    List<String> otherValue = new ArrayList<>();
                    otherValue.add(subEc.getOtherExpectItem());
                    subEcList.setOtherValue(otherValue);
                } else {
                    subEcList.setOtherValue(null);

                }
                if (!ecList.getSubCategories().stream().anyMatch(sub -> sub.getId().equals(subCategoryId))) {
                    ecList.getSubCategories().add(subEcList);
                }
            }
        }
        List<ExpectCategoryList> expectCategoryLists = new ArrayList<>(expectCategoryListMap.values());
        dto.setExpectCategory(expectCategoryLists);
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
        if (data.getAppUserDetail().getType() == UserType.MEMBER_SOLITAIRE) {
            userType = "Parent Solitaire / Prioritas";
            birthdate = userDetail.getMemberBirthdate();
        } else if (data.getAppUserDetail().getType() == UserType.MEMBER_PRIORITY) {
            userType = "Child Solitaire / Prioritas";
            birthdate = userDetail.getParentBirthdate();
        } else {
            userType = "Not Member";
            birthdate = userDetail.getParentBirthdate();
        }

        dto.setBirthDate(Formatter.formatLocalDate(birthdate));

        dto.setType(userType);

        dto.setCreatedAt(Formatter.formatLocalDateTime(userDetail.getCreatedAt()));

        // return
        return dto;
    }


    public void convertToUpdateProfile(AppUser data, AppUserProfileRequest dto) {
        // Update biography if present
        if (dto.getBiography() != null) {
            data.getAppUserDetail().setBiodata(dto.getBiography());
        }

        // Update location if present
        if (dto.getLocation() != null) {
            Location location = locationRepository.findById(dto.getLocation())
                    .orElseThrow(() -> new BadRequestException("Location not found"));
            data.setLocation(location);
        }

        // Update education if present
        if (dto.getEducation() != null) {
            data.getAppUserDetail().setEducation(String.join(", ", dto.getEducation()));
        }

        // Handle userHasExpects if present
        if (dto.getUserHasExpects() != null) {
            // Ensure the list is not null
            List<UserHasExpect> userHasExpects = data.getUserHasExpects();
            if (userHasExpects == null) {
                userHasExpects = new ArrayList<>();
                data.setUserHasExpects(userHasExpects);
            }

            for (AppUserProfileRequest.ProfileExpectCategoryResponse expectDto : dto.getUserHasExpects()) {
                // Your existing logic to process userHasExpects
                ExpectCategory expectCategory = expectCategoryRepository.findById(expectDto.getExpectCategoryId())
                        .orElseThrow(() -> new BadRequestException("Expect Category not found"));

                if (expectDto.getItems() != null && expectDto.getItems().getIds() != null && !expectDto.getItems().getIds().isEmpty()) {
                    for (Long expectItemId : expectDto.getItems().getIds()) {
                        ExpectItem expectItem = expectItemRepository.findById(expectItemId)
                                .orElseThrow(() -> new BadRequestException("Expect Item not found"));

                        UserHasExpect userHasExpect = new UserHasExpect();
                        UserHasExpectId userHasExpectId = new UserHasExpectId(data.getId(), expectCategory.getId(), expectItem.getId());
                        userHasExpect.setId(userHasExpectId);
                        userHasExpect.setUser(data);
                        userHasExpect.setExpectCategory(expectCategory);
                        userHasExpect.setExpectItem(expectItem);
                        userHasExpect.setOtherExpect(expectDto.getOtherExpect());
                        userHasExpect.setOtherExpectItem(expectDto.getItems().getOtherExpectItem());

                        userHasExpects.add(userHasExpect); // Add to the list
                    }
                } else if (expectDto.getExpectCategoryId() == 5) {
                    UserHasExpect userHasExpect = new UserHasExpect();
                    ExpectItem expectItem = expectItemRepository.findById(5L)
                            .orElseThrow(() -> new BadRequestException("Expect Item not found"));
                    userHasExpect.setUser(data);
                    userHasExpect.setExpectCategory(expectCategory);
                    userHasExpect.setExpectItem(expectItem);
                    userHasExpect.setOtherExpect(expectDto.getOtherExpect());
                    userHasExpects.add(userHasExpect); // Add to the list
                }
            }
        }
    }

}
