package com.bca.byc.convert;

import com.bca.byc.entity.StatusType;
import com.bca.byc.entity.User;
import com.bca.byc.entity.UserType;
import com.bca.byc.model.RegisterRequest;
import com.bca.byc.model.api.UserDetailResponse;
import com.bca.byc.model.api.UserUpdateRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
public class UserDTOConverter {

    @Autowired
    private ModelMapper modelMapper;

    // for get data
    public UserDetailResponse convertToListResponse(User user){

        UserDetailResponse dto = modelMapper.map(user, UserDetailResponse.class);

        return dto;
    }

    // for create data
    public User convertToCreateRequest(@Valid RegisterRequest dto) {

        User user = modelMapper.map(dto, User.class);

        user.setType(UserType.MEMBER); // Default value
        user.setStatus(StatusType.PENDING); // Default value

        return user;
    }

    // for update data
    public void convertToUpdateRequest(User user, @Valid UserUpdateRequest dto) {

        modelMapper.map(dto, user);

        // set updatedAt
        user.setUpdatedAt(LocalDateTime.now());

    }
}
