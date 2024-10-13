package com.bca.byc.service;

import com.bca.byc.model.AttributeNameResponse;
import com.bca.byc.model.attribute.AttributeResponse;
import com.bca.byc.response.ResultPageResponseDTO;

public interface InputAttributeService {
    ResultPageResponseDTO<AttributeNameResponse> listDataSubCategoryBusiness(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AttributeNameResponse> listDataSubCategoryExpect(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AttributeResponse<Long>> listDataBranch(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AttributeResponse<Long>> listDataKanwil(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AttributeResponse<Long>> listDataLocation(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AttributeResponse<Long>> listDataChannel(Integer pages, Integer limit, String sortBy, String direction, String keyword);
}
