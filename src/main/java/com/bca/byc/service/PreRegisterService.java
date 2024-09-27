package com.bca.byc.service;

import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.PreRegisterCreateUpdateRequest;
import com.bca.byc.model.PreRegisterDetailResponse;
import com.bca.byc.response.RejectRequest;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

public interface PreRegisterService {

    PreRegisterDetailResponse findDataById(Long id) throws BadRequestException;

    PreRegisterDetailResponse findDataBySecureId(String id);

    List<PreRegisterDetailResponse> findAllData();

    void saveData(@Valid PreRegisterCreateUpdateRequest dto, String email) throws BadRequestException;

    void updateData(String id, @Valid PreRegisterCreateUpdateRequest dto) throws BadRequestException;

    void deleteData(List<Long> id) throws BadRequestException;

    ResultPageResponseDTO<PreRegisterDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String keyword, AdminApprovalStatus status, LocalDate startDate, LocalDate endDate);

    void approveData(String id, String email) throws BadRequestException;

    void rejectData(String id, RejectRequest reason, String email) throws BadRequestException;
}

