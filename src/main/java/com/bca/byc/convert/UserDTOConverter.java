package com.bca.byc.convert;

import com.bca.byc.entity.User;
import com.bca.byc.model.UserAppDetailResponse;
import com.bca.byc.model.UserCmsDetailResponse;
import com.bca.byc.model.UserUpdateRequest;
import com.bca.byc.model.auth.AuthRegisterRequest;
import com.bca.byc.model.auth.RegisterRequest;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.util.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class UserDTOConverter {

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
    public UserAppDetailResponse convertToInfoResponse(User data) {
        // Mapping Entity with DTO Entity
        UserAppDetailResponse dto = modelMapper.map(data, UserAppDetailResponse.class);
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));

        // List of businesses
        if (data.getBusinesses() != null && !data.getBusinesses().isEmpty()) {
            List<UserAppDetailResponse.BusinessListResponse> businesses = data.getBusinesses().stream().map(b -> {
                UserAppDetailResponse.BusinessListResponse business = new UserAppDetailResponse.BusinessListResponse();
                business.setId(b.getId());
                business.setName(b.getName());
                business.setIsPrimary(b.getIsPrimary());

                 List<UserAppDetailResponse.BusinessCategoryResponse> businessCategoryResponses = b.getBusinessCategories().stream()
                         .map(bc -> {
                             UserAppDetailResponse.BusinessCategoryResponse businessCategoryResponse = new UserAppDetailResponse.BusinessCategoryResponse();
                             businessCategoryResponse.setId(bc.getBusinessCategoryParent().getId());
                             businessCategoryResponse.setName(bc.getBusinessCategoryParent().getName());
                             return businessCategoryResponse;
                         }).collect(Collectors.toList());
//                 business.setCategory(businessCategoryResponses);

                 // Set Line of Business based on the first category's parent name
                 String lineOfBusiness = businessCategoryResponses.stream()
                         .map(UserAppDetailResponse.BusinessCategoryResponse::getName)
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
            UserAppDetailResponse.LocationListResponse locationResponse = new UserAppDetailResponse.LocationListResponse();
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

