package com.bca.byc.convert;

import com.bca.byc.entity.Role;
import com.bca.byc.model.cms.RoleModelDTO;
import com.bca.byc.util.Formatter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class RoleDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public RoleModelDTO.DetailResponse convertToListResponse(Role data) {
        // mapping Entity with DTO Entity
        RoleModelDTO.DetailResponse dto = modelMapper.map(data, RoleModelDTO.DetailResponse.class);
        // Use DataFormatter here
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));
        // return
        return dto;
    }

    // for create data
    public Role convertToCreateRequest(@Valid RoleModelDTO.CreateRequest dto) {
        // mapping DTO Entity with Entity
        Role data = modelMapper.map(dto, Role.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Role data, @Valid RoleModelDTO.UpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
