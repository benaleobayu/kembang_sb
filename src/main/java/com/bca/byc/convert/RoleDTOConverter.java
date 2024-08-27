package com.bca.byc.convert;

import com.bca.byc.entity.Role;
import com.bca.byc.model.RoleModelDTO;
import com.bca.byc.util.Formatter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class RoleDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public RoleModelDTO.RoleDetailResponse convertToListResponse(Role data) {
        // mapping Entity with DTO Entity
        RoleModelDTO.RoleDetailResponse dto = modelMapper.map(data, RoleModelDTO.RoleDetailResponse.class);
        // Use DataFormatter here
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));
        // return
        return dto;
    }

    // for create data
    public Role convertToCreateRequest(@Valid RoleModelDTO.RoleCreateRequest dto) {
        // mapping DTO Entity with Entity
        Role data = modelMapper.map(dto, Role.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Role data, @Valid RoleModelDTO.RoleUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
