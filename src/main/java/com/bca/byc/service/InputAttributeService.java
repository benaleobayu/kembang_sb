package com.bca.byc.service;

import com.bca.byc.model.attribute.AttributeResponse;
import com.bca.byc.response.ResultPageResponseDTO;

import java.util.List;
import java.util.Map;

public interface InputAttributeService {


    ResultPageResponseDTO<AttributeResponse<Long>> listDataLocation(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AttributeResponse<String>> RoleList(Integer pages, Integer limit, String sortBy, String direction, String keyword);

}
