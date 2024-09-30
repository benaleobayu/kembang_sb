package com.bca.byc.service;

import com.bca.byc.model.BusinessCategoryItemCreateRequest;
import com.bca.byc.model.BusinessCategoryItemListResponse;
import com.bca.byc.model.BusinessCategoryUpdateRequest;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;
import com.bca.byc.exception.BadRequestException;

public interface BusinessCategoryItemService {

    ResultPageResponseDTO<BusinessCategoryItemListResponse> listDataBusinessCategoryItem(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    BusinessCategoryItemListResponse findDataBySecureId(String parentId, String id) throws BadRequestException;

    void saveData(String parentid, @Valid BusinessCategoryItemCreateRequest dto) throws BadRequestException;

    void updateData(String parentId, String id, @Valid BusinessCategoryUpdateRequest item);

    void deleteData(String parentId, String id) throws BadRequestException;
}

