package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.SettingIndexResponse;
import com.bca.byc.model.SettingsCreateRequest;
import com.bca.byc.model.SettingDetailResponse;
import com.bca.byc.model.SettingsUpdateRequest;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface SettingService {

    ResultPageResponseDTO<SettingIndexResponse> listDataSetting(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    SettingDetailResponse findDataById(String id) throws BadRequestException;

    List<SettingDetailResponse> findAllData();

    void saveData(@Valid SettingsCreateRequest dto) throws BadRequestException;

    void updateData(String id, @Valid SettingsUpdateRequest dto) throws BadRequestException;

    void deleteData(String id) throws BadRequestException;

    SettingDetailResponse showByIdentity(String identity);
}
