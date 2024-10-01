package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ExpectCategoryCreateUpdateRequest;
import com.bca.byc.model.ExpectCategoryDetailResponse;
import com.bca.byc.model.ExpectCategoryIndexResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface ExpectCategoryService {

    ResultPageResponseDTO<ExpectCategoryIndexResponse> listDataExpectCategory(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ExpectCategoryDetailResponse findDataBySecureId(String id) throws BadRequestException;

    List<ExpectCategoryIndexResponse> findAllData();

    void saveData(@Valid ExpectCategoryCreateUpdateRequest dto) throws BadRequestException;

    void updateData(String id, @Valid ExpectCategoryCreateUpdateRequest dto) throws BadRequestException;

    void deleteData(String id) throws BadRequestException;
}
