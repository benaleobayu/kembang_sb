package com.bca.byc.converter;

import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.AppUserDetail;
import com.bca.byc.model.UserManagementDetailResponse;
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

    private ModelMapper modelMapper;

    @Value("${app.base.url}")
    private String baseUrl;

    // for get data
    public UserManagementDetailResponse convertToListResponse(AppUser data) {
        // mapping Entity with DTO Entity
        UserManagementDetailResponse dto = modelMapper.map(data, UserManagementDetailResponse.class);

        TreeUserManagementConverter converter = new TreeUserManagementConverter();
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
        AppUserDetail userDetail = data.getAppUserDetail();
        userDetail.setName(dto.getName());
        userDetail.setMemberBirthdate(dto.getBirthDate());
        userDetail.setPhone(dto.getPhone());
        userDetail.setMemberCin(dto.getMemberCin());
        userDetail.setParentCin(dto.getParentCin());
        userDetail.setMemberBankAccount(dto.getMemberBankAccount());
        userDetail.setParentBankAccount(dto.getParentBankAccount());
        userDetail.setBranchCode(dto.getBranchCode());
        userDetail.setPicName(dto.getPicName());

        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }

}

