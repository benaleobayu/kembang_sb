package com.bca.byc.convert;

import com.bca.byc.entity.Admin;
import com.bca.byc.model.AdminCreateRequest;
import com.bca.byc.model.AdminDetailResponse;
import com.bca.byc.model.AdminUpdateRequest;
import com.bca.byc.util.Formatter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class AdminDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public AdminDetailResponse convertToListResponse(Admin data) {
        // mapping Entity with DTO Entity
        AdminDetailResponse dto = modelMapper.map(data, AdminDetailResponse.class);
        // Use DataFormatter here
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));
        // return
        return dto;
    }

    // for create data
    public Admin convertToCreateRequest(@Valid AdminCreateRequest dto) {
        // mapping DTO Entity with Entity
        Admin data = modelMapper.map(dto, Admin.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Admin data, @Valid AdminUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
