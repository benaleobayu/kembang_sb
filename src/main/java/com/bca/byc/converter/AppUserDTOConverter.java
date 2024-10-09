package com.bca.byc.converter;

import com.bca.byc.entity.*;
import com.bca.byc.enums.UserType;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.*;
import com.bca.byc.model.apps.ExpectCategoryUserInfoResponse;
import com.bca.byc.model.data.BusinessListResponse;
import com.bca.byc.repository.ExpectCategoryRepository;
import com.bca.byc.repository.ExpectItemRepository;
import com.bca.byc.repository.LocationRepository;
import com.bca.byc.repository.UserHasExpectRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.util.helper.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
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
        dto.setId(data.getSecureId());
        dto.setName(data.getAppUserDetail().getName());
        dto.setEmail(data.getEmail());
        AppUserDetail appUserDetail = data.getAppUserDetail();
        dto.setStatus(appUserDetail.getStatus());
        dto.setType(appUserDetail.getType());
        dto.setTypeName(appUserDetail.getType().equals(UserType.MEMBER_SOLITAIRE) ? "Solitaire" :
                appUserDetail.getType().equals(UserType.MEMBER_PRIORITY) ? "Priority" : "Member");
        dto.setAvatar(Objects.isNull(appUserDetail.getAvatar()) || appUserDetail.getAvatar().isBlank() ? null :
                appUserDetail.getAvatar().startsWith("uploads/") ?
                        baseUrl + "/" + appUserDetail.getAvatar() :
                        appUserDetail.getAvatar().startsWith("/uploads/") ? baseUrl + appUserDetail.getAvatar() : appUserDetail.getAvatar());
        dto.setCover(Objects.isNull(appUserDetail.getCover()) || appUserDetail.getCover().isBlank() ? null :
                appUserDetail.getCover().startsWith("uploads/") ?
                        baseUrl + "/" + appUserDetail.getCover() :
                        appUserDetail.getCover().startsWith("/uploads/") ? baseUrl + appUserDetail.getCover() : appUserDetail.getCover());
        dto.setBiodata(Objects.equals(appUserDetail.getBiodata(), "") ? null : appUserDetail.getBiodata());
        dto.setCountryCode(appUserDetail.getCountryCode());
        dto.setPhone(appUserDetail.getPhone());
        List<String> educations = new ArrayList<>();
        String[] eduArray = data.getAppUserDetail().getEducation() != null ? data.getAppUserDetail().getEducation().split(",") : new String[0];
        for (String edu : eduArray) {
            educations.add(edu.trim());
        }
        dto.setEducation(educations);
        dto.setCreatedAt(Formatter.formatDateTimeApps(appUserDetail.getCreatedAt()));

        // List of businesses
        if (data.getBusinesses() != null && !data.getBusinesses().isEmpty()) {
            List<BusinessListResponse> businesses = data.getBusinesses().stream().map(b -> {
                BusinessListResponse business = new BusinessListResponse();
                business.setId(b.getSecureId());
                business.setIndex(b.getId());
                business.setName(b.getName());
                business.setIsPrimary(b.getIsPrimary());

                int businessChildIdx = 1;
                List<BusinessCategoryResponse> businessCategoryResponses = b.getBusinessCategories().stream()
                        .map(bc -> {
                            BusinessCategoryResponse businessCategoryResponse = new BusinessCategoryResponse();
                            businessCategoryResponse.setId(bc.getBusinessCategoryParent().getSecureId());
                            businessCategoryResponse.setIndex(bc.getBusinessCategoryParent().getId());
                            businessCategoryResponse.setName(bc.getBusinessCategoryParent().getName());
                            return businessCategoryResponse;
                        }).collect(Collectors.toList());
//                 business.setCategory(businessCategoryResponses);

                // Set Line of Business based on the first category's parent name
                String lineOfBusiness = businessCategoryResponses.stream()
                        .map(BusinessCategoryResponse::getName)
                        .findFirst()
                        .orElse("Unknown Business Category");
                business.setLineOfBusiness(lineOfBusiness);

                return business;
            }).collect(Collectors.toList());
            dto.setBusinesses(businesses);
        } else {
            log.warn("Businesses list is empty or null for user: {}", data.getId());
        }
        // Update expect categories to new schema
        List<ExpectCategoryUserInfoResponse> expectCategoryResponses = new ArrayList<>();

        Map<String, ExpectCategoryUserInfoResponse> expectCategoryMap = new HashMap<>();

        for (UserHasExpect ec : data.getUserHasExpects()) {
            String categoryId = ec.getExpectCategory().getSecureId();

            // Create or retrieve the category response
            ExpectCategoryUserInfoResponse categoryResponse = expectCategoryMap.get(categoryId);
            if (categoryResponse == null) {
                categoryResponse = new ExpectCategoryUserInfoResponse();
                categoryResponse.setExpectCategoryId(String.valueOf(categoryId));
                categoryResponse.setOtherExpect(ec.getOtherExpect());
                categoryResponse.setItems(new ExpectCategoryUserInfoResponse.Items());
                categoryResponse.getItems().setIds(new ArrayList<>());
                expectCategoryMap.put(categoryId, categoryResponse);
            }

            // Add sub-category ID
            categoryResponse.getItems().getIds().add(ec.getExpectItem().getSecureId());

            // Set otherExpectItem if necessary
            if (ec.getExpectItem().getName().equals("Other")) {
                categoryResponse.getItems().setOtherExpectItem(ec.getOtherExpectItem());
            }
        }

        expectCategoryResponses.addAll(expectCategoryMap.values());
        dto.setExpectCategory(expectCategoryResponses);

        // Other attributes
        if (data.getLocation() != null) {
            UserInfoResponse.LocationListResponse locationResponse = new UserInfoResponse.LocationListResponse();
            locationResponse.setId(data.getLocation().getSecureId());
            locationResponse.setName(data.getLocation().getName());
            dto.setLocation(locationResponse);
        } else {
            log.warn("Location is null for user: {}", data.getId());
        }

        // Count followers, following, posts, events
//        dto.setTotalFollowers(data.getFollowers() != null ? data.getFollowers().size() : 0);
        dto.setTotalFollowing(data.getFollows() != null ? data.getFollows().size() : 0);
//        dto.setTotalPosts(0);  // Placeholder, update as needed
//        dto.setTotalEvents(0);  // Placeholder, update as needed

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
            Location location = HandlerRepository.getIdBySecureId(
                    dto.getLocation(),
                    locationRepository::findBySecureId,
                    projection -> locationRepository.findById(projection.getId()),
                    "Location not found"
            );
            data.setLocation(location);
        }

        // Update education if present
        if (dto.getEducation() != null) {
            data.getAppUserDetail().setEducation(String.join(", ", dto.getEducation()));
        }

        // Handle userHasExpects if present
        if (dto.getUserHasExpects() != null) {
            List<UserHasExpect> userHasExpects = data.getUserHasExpects();
            if (userHasExpects == null) {
                userHasExpects = new ArrayList<>();
                data.setUserHasExpects(userHasExpects);
            }

            // Retrieve the secure ID for expect category with ID 5
            ExpectCategory specialExpectCategory = expectCategoryRepository.findById(5L)
                    .orElseThrow(() -> new BadRequestException("Expect Category not found"));

            String expectCategoryIdForSpecialCase = specialExpectCategory.getSecureId();

            for (AppUserProfileRequest.ProfileExpectCategoryResponse expectDto : dto.getUserHasExpects()) {
                // Your existing logic to process userHasExpects
                ExpectCategory expectCategory = HandlerRepository.getIdBySecureId(
                        expectDto.getExpectCategoryId(),
                        expectCategoryRepository::findBySecureId,
                        projection -> expectCategoryRepository.findById(projection.getId()),
                        "Expect Category not found");

                if (expectDto.getItems() != null && expectDto.getItems().getIds() != null && !expectDto.getItems().getIds().isEmpty()) {
                    for (String secureId : expectDto.getItems().getIds()) {
                        ExpectItem expectItem = HandlerRepository.getIdBySecureId(
                                secureId,
                                expectItemRepository::findBySecureId,
                                projection -> expectItemRepository.findById(projection.getId()),
                                "Expect Item not found"
                        );

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
                } else if (expectDto.getExpectCategoryId().equals(expectCategoryIdForSpecialCase)) {
                    UserHasExpect userHasExpect = new UserHasExpect();
                    ExpectItem expectItem = expectItemRepository.findById(5L) // Adjust if you need secure ID logic
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
