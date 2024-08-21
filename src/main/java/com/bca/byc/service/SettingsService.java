package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.SettingsModelDTO;

import javax.validation.Valid;
import java.util.List;

public interface SettingsService {

    SettingsModelDTO.SettingsDetailResponse findDataById(Long id) throws BadRequestException;

    List<SettingsModelDTO.SettingsDetailResponse> findAllData();

    void saveData(@Valid SettingsModelDTO.SettingsCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid SettingsModelDTO.SettingsUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;

    SettingsModelDTO.SettingsDetailResponse showByIdentity(String identity);
}
