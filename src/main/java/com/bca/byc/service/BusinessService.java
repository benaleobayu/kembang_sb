package com.bca.byc.service;

import com.bca.byc.model.api.BusinessCreateRequest;
import com.bca.byc.model.api.BusinessDetailResponse;

import java.util.List;

public interface BusinessService {

//    boolean existsById(Long id);

//    BusinessDetailResponse findBusinessById(Long id);

    List<BusinessDetailResponse> findAllBusiness();

    void saveBusiness(BusinessCreateRequest dto) throws Exception;

//    void updateBusiness(Long id, BusinessUpdateRequest dto);

//    void deleteBusiness(Long id);
}
