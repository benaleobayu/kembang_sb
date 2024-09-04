package com.bca.byc.service.impl;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.SettingsCreateRequest;
import com.bca.byc.model.SettingsDetailResponse;
import com.bca.byc.model.SettingsUpdateRequest;
import com.bca.byc.service.SettingService;

import java.util.List;

public class SettingServiceImpl implements SettingService {
    @Override
    public SettingsDetailResponse findDataById(Long id) throws BadRequestException {
        return null;
    }

    @Override
    public List<SettingsDetailResponse> findAllData() {
        return null;
    }

    @Override
    public void saveData(SettingsCreateRequest dto) throws BadRequestException {

    }

    @Override
    public void updateData(Long id, SettingsUpdateRequest dto) throws BadRequestException {

    }

    @Override
    public void deleteData(Long id) throws BadRequestException {

    }

    @Override
    public SettingsDetailResponse showByIdentity(String identity) {
        return null;
    }
}
