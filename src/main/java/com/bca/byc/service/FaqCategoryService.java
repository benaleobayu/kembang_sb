package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.FaqCategoryCreateRequest;
import com.bca.byc.model.FaqCategoryDetailResponse;
import com.bca.byc.model.FaqCategoryUpdateRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface FaqCategoryService {

    FaqCategoryDetailResponse findDataById(Long id) throws BadRequestException;

    List<FaqCategoryDetailResponse> findAllData();

    void saveData(@Valid FaqCategoryCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid FaqCategoryUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
