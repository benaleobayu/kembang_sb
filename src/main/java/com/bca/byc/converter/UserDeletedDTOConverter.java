package com.bca.byc.converter;

import com.bca.byc.entity.AppUser;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.service.UserActiveUpdateRequest;
import com.bca.byc.util.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class UserDeletedDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public UserManagementDetailResponse convertToListResponse(AppUser data) {
        // mapping Entity with DTO Entity
        UserManagementDetailResponse dto = modelMapper.map(data, UserManagementDetailResponse.class);
        dto.setName(data.getAppUserDetail().getName());
        dto.setPhone(data.getAppUserDetail().getPhone());
        dto.setType(data.getAppUserDetail().getType().toString());
        dto.setMemberType(data.getAppUserDetail().getMemberType());
        dto.setMemberCardNumber(data.getAppUserDetail().getMemberBankAccount());
        dto.setParentBankAccount(data.getAppUserDetail().getChildBankAccount());
        dto.setCin(data.getAppUserDetail().getUserAs() == null || data.getAppUserDetail().getUserAs().equalsIgnoreCase("member") ?
                data.getAppUserDetail().getMemberCin() : data.getAppUserDetail().getChildCin());
        dto.setBirthDate(data.getAppUserDetail().getUserAs() == null || data.getAppUserDetail().getUserAs().equalsIgnoreCase("member") ?
                Formatter.formatLocalDate(data.getAppUserDetail().getMemberBirthdate()) :
                Formatter.formatLocalDate(data.getAppUserDetail().getChildBirthdate()));
        dto.setOrders(data.getAppUserDetail().getId());
        dto.setStatus(data.getAppUserDetail().getStatus());
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getAppUserDetail().getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getAppUserDetail().getUpdatedAt()));
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
}

