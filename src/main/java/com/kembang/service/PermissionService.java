package com.kembang.service;

import com.kembang.model.PermissionListResponse;
import com.kembang.response.ResultPageResponseDTO;

import javax.xml.transform.Result;
import java.util.List;

public interface PermissionService {

    List<PermissionListResponse> findAllData();

    ResultPageResponseDTO<PermissionListResponse> listDataList(Integer pages, Integer limit, String sortBy, String direction, String keyword);

}
