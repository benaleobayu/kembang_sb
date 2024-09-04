package com.bca.byc.converter;

import com.bca.byc.entity.Settings;
import com.bca.byc.model.SettingsCreateRequest;
import com.bca.byc.model.SettingsDetailResponse;
import com.bca.byc.model.SettingsUpdateRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class SettingsDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public SettingsDetailResponse convertToListResponse(Settings data) {
        // mapping Entity with DTO Entity
        SettingsDetailResponse dto = modelMapper.map(data, SettingsDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public Settings convertToCreateRequest(@Valid SettingsCreateRequest dto) {
        // mapping DTO Entity with Entity
        Settings data = modelMapper.map(dto, Settings.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Settings data, @Valid SettingsUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
