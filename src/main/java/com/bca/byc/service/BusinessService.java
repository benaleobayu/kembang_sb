package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;

import com.bca.byc.model.BusinessCreateRequest;
import com.bca.byc.model.BusinessDetailResponse;
import com.bca.byc.model.BusinessUpdateRequest;
import jakarta.validation.Valid;
import java.util.List;

public interface BusinessService {

    BusinessDetailResponse findDataById(Long id) throws BadRequestException;

    List<BusinessDetailResponse> findAllData();

    void saveData(@Valid BusinessCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid BusinessUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
