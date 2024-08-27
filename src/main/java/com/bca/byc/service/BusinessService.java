package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BusinessModelDTO;

import jakarta.validation.Valid;
import java.util.List;

public interface BusinessService {

    BusinessModelDTO.BusinessDetailResponse findDataById(Long id) throws BadRequestException;

    List<BusinessModelDTO.BusinessDetailResponse> findAllData();

    void saveData(@Valid BusinessModelDTO.BusinessCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid BusinessModelDTO.BusinessUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
