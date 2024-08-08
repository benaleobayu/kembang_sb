package com.bca.byc.service;

import com.bca.byc.model.api.BusinessCreateRequest;
import com.bca.byc.model.api.BusinessDetailResponse;
import com.bca.byc.model.api.BusinessUpdateRequest;

import javax.validation.Valid;
import java.util.List;

public interface BusinessService {

//    boolean existsById(Long id);

    BusinessDetailResponse findDataById(Long id);

    List<BusinessDetailResponse> getAllData();

    void saveData(BusinessCreateRequest dto) throws Exception;

    void updateData(Long id, @Valid BusinessUpdateRequest dto);

    void deleteData(Long id);
}
