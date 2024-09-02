package com.bca.byc.convert;

import com.bca.byc.entity.User;
import com.bca.byc.model.OnboardingListUserResponse;
import com.bca.byc.model.UserCmsDetailResponse;
import com.bca.byc.model.UserUpdateRequest;
import com.bca.byc.model.auth.AuthRegisterRequest;
import com.bca.byc.model.auth.RegisterRequest;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.security.UserPrincipal;
import com.bca.byc.util.Formatter;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class OnboardingDTOConverter {

    private UserRepository userRepository;

    private ModelMapper modelMapper;

    // for get data
    public UserCmsDetailResponse convertToListResponse(User data) {
        // mapping Entity with DTO Entity
        UserCmsDetailResponse dto = modelMapper.map(data, UserCmsDetailResponse.class);
        // Use DataFormatter here
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));


        // other attributes
        dto.setIsRecommended(Optional.ofNullable(data.getUserAttributes().getIsRecommended()).orElse(false));
        // count follower from follow table
        dto.setTotalFollowers(data.getFollowers().size());
        dto.setTotalFollowing(data.getFollows().size());

        dto.setIsFollowed(data.getFollowers().stream().anyMatch(f -> f.getId().equals(data.getId())));
        dto.setIsFollowing(data.getFollowers().stream().anyMatch(f -> f.getId().equals(data.getId())));
        return dto;
    }

    // for get data after login
    public OnboardingListUserResponse convertToListOnboardingResponse(User data) {
        // Mapping Entity with DTO Entity
        OnboardingListUserResponse dto = modelMapper.map(data, OnboardingListUserResponse.class);
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));

        // List of businesses
        if (data.getBusinesses() != null && !data.getBusinesses().isEmpty()) {
            List<OnboardingListUserResponse.BusinessListResponse> businesses = data.getBusinesses().stream().map(b -> {
                OnboardingListUserResponse.BusinessListResponse business = new OnboardingListUserResponse.BusinessListResponse();
                business.setId(b.getId());
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
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long loggedInUserId = ((UserPrincipal) userDetails).user.getId(); // Assuming AppUser implements UserDetails and has getId()
            boolean isFollowing = data.getFollowers().stream()
                    .anyMatch(follower -> follower.getId().equals(loggedInUserId));
            dto.setIsFollowed(isFollowing);
        }


        return dto;
    }

    // for create data
    public User convertToCreateGroupRequest(@Valid AuthRegisterRequest dto) {
        // mapping DTO Entity with Entity
        User data = modelMapper.map(dto, User.class);
        // return
        return data;
    }


    // for update data
    public void convertToUpdateRequest(User data, @Valid UserUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }


    // ------------------------------------------
    // for create data
    public User convertToCreateRequest(@Valid RegisterRequest dto) {
        // mapping DTO Entity with Entity
        User data = modelMapper.map(dto, User.class);
        // return
        return data;
    }

    // for update data
    public User convertToUpdateIfExistRequest(User data, @Valid RegisterRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
        return data;
    }
}

