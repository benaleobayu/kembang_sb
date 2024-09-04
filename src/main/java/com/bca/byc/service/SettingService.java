package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.SettingsCreateRequest;
import com.bca.byc.model.SettingsDetailResponse;
import com.bca.byc.model.SettingsUpdateRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface SettingService {

    SettingsDetailResponse findDataById(Long id) throws BadRequestException;

    List<SettingsDetailResponse> findAllData();

    void saveData(@Valid SettingsCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid SettingsUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;

    SettingsDetailResponse showByIdentity(String identity);
}
