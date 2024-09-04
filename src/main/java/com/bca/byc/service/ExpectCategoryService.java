package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ExpectCategoryCreateRequest;
import com.bca.byc.model.ExpectCategoryDetailResponse;
import com.bca.byc.model.ExpectCategoryUpdateRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface ExpectCategoryService {

    ExpectCategoryDetailResponse findDataById(Long id) throws BadRequestException;

    List<ExpectCategoryDetailResponse> findAllData();

    void saveData(@Valid ExpectCategoryCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid ExpectCategoryUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
