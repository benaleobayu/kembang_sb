package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.FaqCategoryCreateUpdateRequest;
import com.bca.byc.model.FaqCategoryDetailResponse;
import com.bca.byc.model.FaqCategoryIndexResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface FaqCategoryService {

    List<FaqCategoryDetailResponse> findAllData();

    ResultPageResponseDTO<FaqCategoryIndexResponse> listDataFaqCategory(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    FaqCategoryDetailResponse findDataById(String id) throws BadRequestException;

    void saveData(@Valid FaqCategoryCreateUpdateRequest dto) throws BadRequestException;

    void updateData(String id, @Valid FaqCategoryCreateUpdateRequest dto) throws BadRequestException;

    void deleteData(String id) throws BadRequestException;
}
