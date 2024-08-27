package com.bca.byc.service;

import jakarta.validation.Valid;

import com.bca.byc.entity.Admin;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.AdminModelDTO;

import java.util.List;

public interface AdminService {

    AdminModelDTO.AdminDetailResponse findDataById(Long id) throws BadRequestException;

    List<AdminModelDTO.AdminDetailResponse> findAllData();

    void saveData(@Valid AdminModelDTO.AdminCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid AdminModelDTO.AdminUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}

