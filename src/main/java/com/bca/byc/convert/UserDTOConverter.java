package com.bca.byc.convert;

import com.bca.byc.entity.User;
import com.bca.byc.model.UserAppDetailResponse;
import com.bca.byc.model.auth.AuthRegisterRequest;
import com.bca.byc.model.auth.RegisterRequest;
import com.bca.byc.model.UserCmsDetailResponse;
import com.bca.byc.model.UserUpdateRequest;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.util.Formatter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@AllArgsConstructor
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
        dto.setTotalFollowers( data.getFollowers().size() );
        dto.setTotalFollowing( data.getFollows().size() );

        dto.setIsFollowed(data.getFollowers().stream().anyMatch(f -> f.getId().equals(data.getId())));
        dto.setIsFollowing(data.getFollowers().stream().anyMatch(f -> f.getId().equals(data.getId())));
        return dto;
    }

    public UserCmsDetailResponse convertToDetailResponse(User data) {
        // mapping Entity with DTO Entity
        UserCmsDetailResponse dto = modelMapper.map(data, UserCmsDetailResponse.class);
        // Use DataFormatter here
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));

        // other attributes
        dto.setIsRecommended(Optional.ofNullable(data.getUserAttributes().getIsRecommended()).orElse(false));
        // count follower from follow table
        dto.setTotalFollowers( data.getFollowers().size() );
        dto.setTotalFollowing( data.getFollows().size() );

        dto.setIsFollowed(data.getFollowers().stream().anyMatch(f -> f.getId().equals(data.getId())));
        dto.setIsFollowing(data.getFollowers().stream().anyMatch(f -> f.getId().equals(data.getId())));
        return dto;
    }

    public UserCmsDetailResponse convertToInfoDetailResponse(User data) {
        // mapping Entity with DTO Entity
        UserCmsDetailResponse dto = modelMapper.map(data, UserCmsDetailResponse.class);
        // Use DataFormatter here
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));

        // other attributes
        dto.setIsRecommended(Optional.ofNullable(data.getUserAttributes().getIsRecommended()).orElse(false));
        // count follower from follow table
        dto.setTotalFollowers( data.getFollowers().size() );
        dto.setTotalFollowing( data.getFollows().size() );

        dto.setIsFollowed(data.getFollowers().stream().anyMatch(f -> f.getId().equals(data.getId())));
        dto.setIsFollowing(data.getFollowers().stream().anyMatch(f -> f.getId().equals(data.getId())));
        return dto;
    }





     // for get data after login
    public UserAppDetailResponse convertToInfoResponse(User data) {
        // mapping Entity with DTO Entity
        UserAppDetailResponse dto = modelMapper.map(data, UserAppDetailResponse.class);
        // Use DataFormatter here
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));

//        List<Business> business = data.getBusinesses().stream().map(b -> {
//            b.setId(data.getId());
//            b.setName(data.getName());
//            return b;
//        }).collect(Collectors.toList());
//        dto.setBusinesses(business);

        // other attributes

        // count follower from follow table
        dto.setTotalFollowers( data.getFollowers().size() );
        dto.setTotalFollowing( data.getFollows().size() );
        dto.setTotalPosts(0);
        dto.setTotalEvents(0);
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

