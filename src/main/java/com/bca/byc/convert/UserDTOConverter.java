package com.bca.byc.convert;
import com.bca.byc.entity.User;
import com.bca.byc.model.RegisterRequest;
import com.bca.byc.model.api.UserDetailResponse;
import com.bca.byc.model.api.UserUpdateRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class UserDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public UserDetailResponse convertToListResponse(User data) {
        // mapping Entity with DTO Entity
        UserDetailResponse dto = modelMapper.map(data, UserDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public User convertToCreateRequest(@Valid RegisterRequest dto) {
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
}

