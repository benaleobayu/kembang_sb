package com.bca.byc.service.cms;


import com.bca.byc.entity.Elastic.AppUserElastic;
import com.bca.byc.response.Page;

public interface CmsUserService {


    Page<AppUserElastic> getAllUsers(Integer page, Integer size);

}
