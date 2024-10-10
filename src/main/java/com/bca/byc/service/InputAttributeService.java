package com.bca.byc.service;

import com.bca.byc.model.AttributeNameResponse;
import com.bca.byc.model.attribute.AttributeResponse;
import com.bca.byc.response.ResultPageResponseDTO;

public interface InputAttributeService {
    ResultPageResponseDTO<AttributeNameResponse> listDataSubCategoryBusiness(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AttributeNameResponse> listDataSubCategoryExpect(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AttributeResponse> listDataBranch(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AttributeResponse> listDataKanwil(Integer pages, Integer limit, String sortBy, String direction, String keyword);
}
