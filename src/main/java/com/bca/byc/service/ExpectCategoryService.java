package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ExpectCategoryCreateUpdateRequest;
import com.bca.byc.model.ExpectCategoryIndexResponse;
import com.bca.byc.model.PublicExpectCategoryDetailResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface ExpectCategoryService {

    // public
    List<PublicExpectCategoryDetailResponse> findAllData();

    // cms
    ResultPageResponseDTO<ExpectCategoryIndexResponse> listDataExpectCategory(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ExpectCategoryIndexResponse findDataBySecureId(String id) throws BadRequestException;

    void saveData(@Valid ExpectCategoryCreateUpdateRequest dto) throws BadRequestException;

    void updateData(String id, @Valid ExpectCategoryCreateUpdateRequest dto) throws BadRequestException;

    void deleteData(String id) throws BadRequestException;
}
