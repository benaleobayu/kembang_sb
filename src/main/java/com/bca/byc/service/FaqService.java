package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.FaqCreateRequest;
import com.bca.byc.model.FaqDetailResponse;
import com.bca.byc.model.FaqUpdateRequest;

import javax.validation.Valid;
import java.util.List;

public interface FaqService {

    FaqDetailResponse findDataById(Long id) throws BadRequestException;

    List<FaqDetailResponse> findAllData();

    void saveData(@Valid FaqCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid FaqUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
