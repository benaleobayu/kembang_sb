package com.bca.byc.converter;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.AppUserDetail;
import com.bca.byc.model.AppRegisterRequest;
import com.bca.byc.model.OnboardingListUserResponse;
import com.bca.byc.model.UserUpdateRequest;
import com.bca.byc.repository.AppUserRepository;
import com.bca.byc.service.AppUserService;
import com.bca.byc.util.helper.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class OnboardingDTOConverter {

    private final AppUserService userService;
    private AppUserRepository userRepository;
    private ModelMapper modelMapper;

    // for get data
    public OnboardingListUserResponse convertToListResponse(AppUser data) {
        // mapping Entity with DTO Entity
        OnboardingListUserResponse dto = modelMapper.map(data, OnboardingListUserResponse.class);
        // Use DataFormatter here
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));


        // count follower from follow table
        dto.setTotalFollowers(data.getFollowers().size());
        dto.setTotalFollowing(data.getFollows().size());

        dto.setIsFollowed(data.getFollowers().stream().anyMatch(f -> f.getId().equals(data.getId())));
        return dto;
    }

    // for get data after login
    public OnboardingListUserResponse convertToListOnboardingResponse(AppUser data) {
        // Mapping Entity with DTO Entity
        OnboardingListUserResponse dto = modelMapper.map(data, OnboardingListUserResponse.class);
        dto.setId(data.getSecureId());
        dto.setName(data.getName());
        dto.setEmail(data.getEmail());
        AppUserDetail appUserDetail = data.getAppUserDetail();
        dto.setStatus(appUserDetail.getStatus());
        dto.setAvatar(appUserDetail.getAvatar());
        dto.setCover(appUserDetail.getCover());
        dto.setBiodata(appUserDetail.getBiodata());
        dto.setCreatedAt(Formatter.formatLocalDateTime(appUserDetail.getCreatedAt()));

        // List of businesses
        if (data.getBusinesses() != null && !data.getBusinesses().isEmpty()) {
            List<OnboardingListUserResponse.BusinessListResponse> businesses = data.getBusinesses().stream().map(b -> {
                OnboardingListUserResponse.BusinessListResponse business = new OnboardingListUserResponse.BusinessListResponse();
                business.setId(b.getUser().getId());
                business.setName(b.getName());
                business.setIsPrimary(b.getIsPrimary());

                List<OnboardingListUserResponse.BusinessCategoryResponse> businessCategoryResponses = b.getBusinessCategories().stream()
                        .map(bc -> {
                            OnboardingListUserResponse.BusinessCategoryResponse businessCategoryResponse = new OnboardingListUserResponse.BusinessCategoryResponse();
                            businessCategoryResponse.setId(bc.getBusinessCategoryParent().getId());
                            businessCategoryResponse.setName(bc.getBusinessCategoryParent().getName());
                            return businessCategoryResponse;
                        }).collect(Collectors.toList());
//                 business.setCategory(businessCategoryResponses);

                // Set Line of Business based on the first category's parent name
                String lineOfBusiness = businessCategoryResponses.stream()
                        .map(OnboardingListUserResponse.BusinessCategoryResponse::getName)
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
            OnboardingListUserResponse.LocationListResponse locationResponse = new OnboardingListUserResponse.LocationListResponse();
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

        // check the following status
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            AppUser userDetails = (AppUser) authentication.getPrincipal();
            Long loggedInUserId = (userDetails).getId(); // Assuming AppUser implements UserDetails and has getId()
            boolean isFollowing = data.getFollowers().stream()
                    .anyMatch(follower -> follower.getId().equals(loggedInUserId));
            dto.setIsFollowed(isFollowing);
        }


        return dto;
    }


    // for update data
    public void convertToUpdateRequest(AppUser data, @Valid UserUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }


    // ------------------------------------------
    // for create data
    public AppUser convertToCreateRequest(@Valid AppRegisterRequest dto) {
        // mapping DTO Entity with Entity
        AppUser data = modelMapper.map(dto, AppUser.class);
        // return
        return data;
    }

    // for update data
    public AppUser convertToUpdateIfExistRequest(AppUser data, @Valid AppRegisterRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
        return data;
    }
}

