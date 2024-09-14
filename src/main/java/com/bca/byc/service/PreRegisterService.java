package com.bca.byc.service;

import com.bca.byc.model.PreRegisterCreateRequest;
import com.bca.byc.model.PreRegisterDetailResponse;
import com.bca.byc.model.PreRegisterUpdateRequest;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;
import com.bca.byc.exception.BadRequestException;

import java.util.List;

public interface PreRegisterService {

    PreRegisterDetailResponse findDataById(Long id) throws BadRequestException;

    List<PreRegisterDetailResponse> findAllData();

    void saveData(@Valid PreRegisterCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid PreRegisterUpdateRequest dto) throws BadRequestException;

    void deleteData(List<Long> id) throws BadRequestException;

    ResultPageResponseDTO<PreRegisterDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String userName);
}

