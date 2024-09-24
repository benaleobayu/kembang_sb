package com.bca.byc.converter;

import com.bca.byc.entity.AppUser;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.data.ListTagUserResponse;
import com.bca.byc.service.UserActiveUpdateRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class UserActiveDTOConverter {

    @Value("${app.base.url}")
    static String baseUrl;
    private ModelMapper modelMapper;

    // for get data
    public UserManagementDetailResponse convertToListResponse(AppUser data) {
        // mapping Entity with DTO Entity
        UserManagementDetailResponse dto = modelMapper.map(data, UserManagementDetailResponse.class);

        UserManagementConverter converter = new UserManagementConverter();
        converter.DetailResponse(data, dto);

        // return
        return dto;
    }

    // for create data
    public AppUser convertToCreateRequest(@Valid UserActiveUpdateRequest dto) {
        // mapping DTO Entity with Entity
        AppUser data = modelMapper.map(dto, AppUser.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(AppUser data, @Valid UserActiveUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }

    public ListTagUserResponse convertToListTagUserRespose(AppUser data) {
        ListTagUserResponse dto = modelMapper.map(data, ListTagUserResponse.class);

        dto.setId(data.getId());
        dto.setName(data.getAppUserDetail().getName());
        String avatar;

        if (data.getAppUserDetail().getAvatar() != null && data.getAppUserDetail().getAvatar().startsWith("/uploads")) {
            avatar = baseUrl + data.getAppUserDetail().getAvatar();
        } else {
            avatar = data.getAppUserDetail().getAvatar();
        }

        dto.setAvatar(avatar);

        // return
        return dto;
    }
}

