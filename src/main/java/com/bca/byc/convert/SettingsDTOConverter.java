package com.bca.byc.convert;

import com.bca.byc.entity.Settings;
import com.bca.byc.model.cms.SettingsModelDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class SettingsDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public SettingsModelDTO.DetailResponse convertToListResponse(Settings data) {
        // mapping Entity with DTO Entity
        SettingsModelDTO.DetailResponse dto = modelMapper.map(data, SettingsModelDTO.DetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public Settings convertToCreateRequest(@Valid SettingsModelDTO.CreateRequest dto) {
        // mapping DTO Entity with Entity
        Settings data = modelMapper.map(dto, Settings.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Settings data, @Valid SettingsModelDTO.UpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
