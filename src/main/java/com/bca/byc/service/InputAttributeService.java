package com.bca.byc.service;

import com.bca.byc.model.attribute.AttributeResponse;
import com.bca.byc.response.ResultPageResponseDTO;

public interface InputAttributeService {
    ResultPageResponseDTO<AttributeResponse> listDataSubCategoryBusiness(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AttributeResponse> listDataSubCategoryExpect(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AttributeResponse> listDataBranch(Integer pages, Integer limit, String sortBy, String direction, String keyword);
}
