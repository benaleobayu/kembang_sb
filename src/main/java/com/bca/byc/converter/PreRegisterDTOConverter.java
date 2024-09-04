package com.bca.byc.converter;

import com.bca.byc.entity.PreRegister;
import com.bca.byc.enums.StatusType;
import com.bca.byc.model.PreRegisterCreateRequest;
import com.bca.byc.model.PreRegisterDetailResponse;
import com.bca.byc.model.PreRegisterUpdateRequest;
import com.bca.byc.util.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class PreRegisterDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public PreRegisterDetailResponse convertToListResponse(PreRegister data) {
        // mapping Entity with DTO Entity
        PreRegisterDetailResponse dto = modelMapper.map(data, PreRegisterDetailResponse.class);
        // Use DataFormatter here
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));

        // return
        return dto;
    }

    // for create data
    public PreRegister convertToCreateRequest(@Valid PreRegisterCreateRequest dto) {
        // mapping DTO Entity with Entity
        PreRegister data = modelMapper.map(dto, PreRegister.class);

        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(PreRegister data, @Valid PreRegisterUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
