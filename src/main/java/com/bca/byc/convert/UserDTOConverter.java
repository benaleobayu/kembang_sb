package com.bca.byc.convert;

import com.bca.byc.entity.User;
import com.bca.byc.model.auth.AuthRegisterRequest;
import com.bca.byc.model.auth.RegisterRequest;
import com.bca.byc.model.UserDetailResponse;
import com.bca.byc.model.UserUpdateRequest;
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

    private ModelMapper modelMapper;

    // for get data
    public UserDetailResponse convertToListResponse(User data) {
        // mapping Entity with DTO Entity
        UserDetailResponse dto = modelMapper.map(data, UserDetailResponse.class);
        // Use DataFormatter here
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));

        // other attributes
        dto.setIsRecommended(Optional.ofNullable(data.getUserAttributes().getIsRecommended()).orElse(false));
        // count follower from follow table
        dto.setTotalFollowers( data.getFollowers().size() );
        dto.setTotalFollowing( data.getFollows().size() );
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

