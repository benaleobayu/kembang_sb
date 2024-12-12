package com.kembang.service;

import com.kembang.model.attribute.AttributeResponse;
import com.kembang.response.ResultPageResponseDTO;

import java.util.List;
import java.util.Map;

public interface InputAttributeService {


    ResultPageResponseDTO<AttributeResponse<Long>> listDataLocation(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<AttributeResponse<String>> RoleList(Integer pages, Integer limit, String sortBy, String direction, String keyword);

}
