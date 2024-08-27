package com.bca.byc.convert;

import com.bca.byc.entity.Settings;
import com.bca.byc.model.SettingsModelDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class SettingsDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public SettingsModelDTO.SettingsDetailResponse convertToListResponse(Settings data) {
        // mapping Entity with DTO Entity
        SettingsModelDTO.SettingsDetailResponse dto = modelMapper.map(data, SettingsModelDTO.SettingsDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public Settings convertToCreateRequest(@Valid SettingsModelDTO.SettingsCreateRequest dto) {
        // mapping DTO Entity with Entity
        Settings data = modelMapper.map(dto, Settings.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Settings data, @Valid SettingsModelDTO.SettingsUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
