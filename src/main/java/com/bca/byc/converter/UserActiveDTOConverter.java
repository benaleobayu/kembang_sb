package com.bca.byc.converter;

import com.bca.byc.converter.parsing.TreeUserManagementConverter;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.AppUserAttribute;
import com.bca.byc.entity.AppUserDetail;
import com.bca.byc.entity.Branch;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.UserManagementListResponse;
import com.bca.byc.service.UserActiveUpdateRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.bca.byc.converter.parsing.TreeUserManagementConverter.IndexResponse;

@Component
@AllArgsConstructor
public class UserActiveDTOConverter {

    private ModelMapper modelMapper;

    @Value("${app.base.url}")
    private String baseUrl;

    // for get data
    public UserManagementListResponse convertToListResponse(AppUser data) {
        // mapping Entity with DTO Entity
        UserManagementListResponse dto = new UserManagementListResponse();
        IndexResponse(data, dto);

        // return
        return dto;
    }

     // for get data
    public UserManagementDetailResponse convertToDetailResponse(AppUser data) {
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
    public void convertToUpdateRequest(AppUser data, @Valid UserActiveUpdateRequest dto, Branch branch) {
        // mapping DTO Entity with Entity
        AppUserDetail userDetail = data.getAppUserDetail();
        userDetail.setName(dto.getName()); // name
        data.setEmail(dto.getEmail()); // email
        userDetail.setPhone(dto.getPhone());
        userDetail.setMemberBirthdate(dto.getBirthDate());
        userDetail.setMemberCin(dto.getMemberCin());
        userDetail.setMemberBankAccount(dto.getMemberBankAccount());
        userDetail.setMemberType(dto.getMemberType());
        userDetail.setParentCin(dto.getParentCin());
        userDetail.setParentBankAccount(dto.getParentBankAccount());
        userDetail.setParentType(dto.getParentType());
        userDetail.setBranchCode(branch);
        userDetail.setPicName(dto.getPicName());
        userDetail.setIsActive(dto.getStatus());

        data.setAppUserDetail(userDetail);
    }

}

