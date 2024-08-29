package com.bca.byc.service;

import com.bca.byc.model.AdminCreateRequest;
import com.bca.byc.model.AdminDetailResponse;
import com.bca.byc.model.AdminUpdateRequest;
import jakarta.validation.Valid;

import com.bca.byc.exception.BadRequestException;

import java.util.List;

public interface SettingsAdminService {

    AdminDetailResponse findDataById(Long id) throws BadRequestException;

    List<AdminDetailResponse> findAllData();

    void saveData(@Valid AdminCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid AdminUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
