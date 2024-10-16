package com.bca.byc.service;

import com.bca.byc.model.AttributeNameResponse;
import com.bca.byc.model.attribute.AttributeResponse;
import com.bca.byc.response.ResultPageResponseDTO;

import java.util.List;
import java.util.Map;

public interface InputAttributeService {
    ResultPageResponseDTO<AttributeNameResponse> listDataSubCategoryBusiness(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AttributeNameResponse> listDataSubCategoryExpect(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AttributeResponse<Long>> listDataBranch(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AttributeResponse<Long>> listDataKanwil(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AttributeResponse<Long>> listDataLocation(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AttributeResponse<Long>> listDataChannel(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AttributeResponse<String>> RoleList(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    List<Map<String, List<?>>> listReportDetailOf(String detailOf);
}
