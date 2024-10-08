package com.bca.byc.service;

import com.bca.byc.entity.BusinessCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.*;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface BusinessCategoryService {

    // public
    List<BusinessCategoryListResponse> findByParentIdIsNotNull();

    List<BusinessCategoryListResponse> findByParentIdIsNull();

    // category

    ResultPageResponseDTO<BusinessCategoryIndexResponse> listDataBusinessCategory(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    void saveData(@Valid BusinessCategoryParentCreateRequest dto) throws BadRequestException;

    void updateData(String id, @Valid BusinessCategoryUpdateRequest dto) throws BadRequestException;

    void deleteData(String id) throws BadRequestException;

    BusinessCategoryIndexResponse findDataBySecureId(String id);
    BusinessCategory getCategoryById(Long id);

    // post category
    ResultPageResponseDTO<PostCategoryDetailResponse> listDataPostCategory(Integer pages, Integer limit, String sortBy, String direction, String keyword);
}
