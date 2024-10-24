package com.bca.byc.service;

import com.bca.byc.enums.AdminApprovalStatus;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.PreRegisterCreateRequest;
import com.bca.byc.model.PreRegisterDetailResponse;
import com.bca.byc.model.PreRegisterUpdateRequest;
import com.bca.byc.response.RejectRequest;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface PreRegisterService {

    PreRegisterDetailResponse findDataById(Long id) throws BadRequestException;

    PreRegisterDetailResponse findDataBySecureId(String id);

    List<PreRegisterDetailResponse> findAllData();

    void saveData(@Valid PreRegisterCreateRequest dto) throws BadRequestException;

    void updateData(String id, @Valid PreRegisterUpdateRequest dto) throws BadRequestException;

    void bulkDelete(Set<String> ids) throws BadRequestException;

    ResultPageResponseDTO<PreRegisterDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String keyword, AdminApprovalStatus status, LocalDate startDate, LocalDate endDate);

    void approveData(String id, String email) throws BadRequestException, MessagingException;

    void rejectData(String id, RejectRequest reason, String email) throws BadRequestException;
}

