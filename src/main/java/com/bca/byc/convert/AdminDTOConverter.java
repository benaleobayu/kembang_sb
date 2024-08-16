package com.bca.byc.convert;

import com.bca.byc.entity.Admin;
import com.bca.byc.model.cms.AdminModelDTO;
import com.bca.byc.util.Formatter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class AdminDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public AdminModelDTO.DetailResponse convertToListResponse(Admin data) {
        // mapping Entity with DTO Entity
        AdminModelDTO.DetailResponse dto = modelMapper.map(data, AdminModelDTO.DetailResponse.class);
        // Use DataFormatter here
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));
        // return
        return dto;
    }

    // for create data
    public Admin convertToCreateRequest(@Valid AdminModelDTO.CreateRequest dto) {
        // mapping DTO Entity with Entity
        Admin data = modelMapper.map(dto, Admin.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Admin data, @Valid AdminModelDTO.UpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
