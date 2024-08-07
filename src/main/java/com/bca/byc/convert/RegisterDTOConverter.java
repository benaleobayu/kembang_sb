package com.bca.byc.convert;

import com.bca.byc.entity.StatusType;
import com.bca.byc.entity.User;
import com.bca.byc.entity.UserType;
import com.bca.byc.model.RegisterRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegisterDTOConverter {

    @Autowired
    private ModelMapper modelMapper;


    public User convertToUser(RegisterRequest dto) {
        User user = modelMapper.map(dto, User.class);

        user.setType(UserType.MEMBER); // Default value
        user.setStatus(StatusType.PENDING); // Default value

        return user;
    }


}
