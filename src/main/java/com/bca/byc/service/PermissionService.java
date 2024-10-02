package com.bca.byc.service;

import com.bca.byc.model.PermissionListResponse;
import com.bca.byc.response.ResultPageResponseDTO;

import javax.xml.transform.Result;
import java.util.List;

public interface PermissionService {

    List<PermissionListResponse> findAllData();

    ResultPageResponseDTO<PermissionListResponse> listDataList(Integer pages, Integer limit, String sortBy, String direction, String keyword);

}
