package com.bca.byc.service;

import javax.validation.Valid;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.SettingsModelDTO;

import java.util.List;

public interface SettingsService {

    SettingsModelDTO.DetailResponse findDataById(Long id) throws BadRequestException;

    List<SettingsModelDTO.DetailResponse> findAllData();

    void saveData(@Valid SettingsModelDTO.CreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid SettingsModelDTO.UpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
