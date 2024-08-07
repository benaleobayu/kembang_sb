package com.bca.byc.convert;

import com.bca.byc.entity.User;
import com.bca.byc.model.api.UserDetailResponse;
import com.bca.byc.model.api.UserUpdateRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDTOConverter {

    @Autowired
    private ModelMapper modelMapper;

    public UserDetailResponse convertToDTO(User user){
        UserDetailResponse dto = modelMapper.map(user, UserDetailResponse.class);

        return dto;
    }

    public User converToUser(UserDetailResponse dto) {
        User user = modelMapper.map(dto, User.class);

        return user;
    }

    public User convertToUpdateUser(UserUpdateRequest dto){
        User user = modelMapper.map(dto, User.class);

        return user;
    }
}
