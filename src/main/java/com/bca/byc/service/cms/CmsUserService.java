package com.bca.byc.service.cms;


import com.bca.byc.model.Elastic.AppUserElastic;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.response.Page;
import com.bca.byc.response.ResultPageResponseDTO;

public interface CmsUserService {

    Page<AppUserElastic> getAllUsers(Integer page, Integer size);

    Long countAllUsers();

    Long getElasticCount();

    ResultPageResponseDTO<UserManagementDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String keyword);
}
