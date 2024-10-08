package com.bca.byc.converter;

import com.bca.byc.entity.Settings;
import com.bca.byc.model.SettingIndexResponse;
import com.bca.byc.model.SettingsCreateRequest;
import com.bca.byc.model.SettingDetailResponse;
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

    // for get data index
    public SettingIndexResponse convertToIndexResponse(Settings data) {
        // mapping Entity with DTO Entity
        SettingIndexResponse dto = modelMapper.map(data, SettingIndexResponse.class);
        // return
        return dto;
    }

    // for get data
    public SettingDetailResponse convertToDetailResponse(Settings data) {
        // mapping Entity with DTO Entity
        SettingDetailResponse dto = modelMapper.map(data, SettingDetailResponse.class);
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
