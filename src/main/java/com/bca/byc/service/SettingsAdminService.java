package com.bca.byc.service;

import javax.validation.Valid;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.AdminModelDTO;

import java.util.List;

public interface SettingsAdminService {

    AdminModelDTO.DetailResponse findDataById(Long id) throws BadRequestException;

    List<AdminModelDTO.DetailResponse> findAllData();

    void saveData(@Valid AdminModelDTO.CreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid AdminModelDTO.UpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
