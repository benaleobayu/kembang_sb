package com.bca.byc.service.impl;

import com.bca.byc.converter.SettingsDTOConverter;
import com.bca.byc.entity.Settings;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.SettingsCreateRequest;
import com.bca.byc.model.SettingsDetailResponse;
import com.bca.byc.model.SettingsUpdateRequest;
import com.bca.byc.repository.SettingsRepository;
import com.bca.byc.service.SettingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SettingsServiceImpl implements SettingService {

    @Autowired
    private SettingsRepository repository;
    @Autowired
    private SettingsDTOConverter converter;

    @Override
    public SettingsDetailResponse findDataById(Long id) throws BadRequestException {
        Settings data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Settings not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<SettingsDetailResponse> findAllData() {
        // Get the list
        List<Settings> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid SettingsCreateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        Settings data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, SettingsUpdateRequest dto) throws BadRequestException {
        // check exist and get
        Settings data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID Settings ID"));

        // update
        converter.convertToUpdateRequest(data, dto);

        // update the updated_at
        data.setUpdatedAt(LocalDateTime.now());

        // save
        repository.save(data);
    }

    @Override
    public void deleteData(Long id) throws BadRequestException {
        // delete data
        if (!repository.existsById(id)) {
            throw new BadRequestException("Settings not found");
        } else {
            repository.deleteById(id);
        }
    }

    @Override
    public SettingsDetailResponse showByIdentity(String identity) {
        Settings data = repository.findByIdentity(identity)
                .orElseThrow(() -> new BadRequestException("identity not found"));

        return converter.convertToListResponse(data);
    }
}
