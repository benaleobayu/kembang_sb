package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.PreRegisterCreateRequest;
import com.bca.byc.model.PreRegisterDetailResponse;
import com.bca.byc.model.PreRegisterUpdateRequest;
import com.bca.byc.reponse.excel.PreRegisterExportResponse;
import com.bca.byc.response.RejectRequest;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface PreRegisterService {

    PreRegisterDetailResponse findDataById(Long id) throws BadRequestException;

    List<PreRegisterDetailResponse> findAllData();

    void saveData(@Valid PreRegisterCreateRequest dto, String email) throws BadRequestException;

    void updateData(Long id, @Valid PreRegisterUpdateRequest dto) throws BadRequestException;

    void deleteData(List<Long> id) throws BadRequestException;

    ResultPageResponseDTO<PreRegisterDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String userName);

    void approveData(Long id, String email) throws BadRequestException;

    void rejectData(Long id, RejectRequest reason, String email) throws BadRequestException;

    List<PreRegisterExportResponse> findAllDataToExport();
}

