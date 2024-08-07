package com.bca.byc.service.impl;

import com.bca.byc.convert.cms.BusinessDTOConverter;
import com.bca.byc.entity.Business;
import com.bca.byc.model.api.BusinessCreateRequest;
import com.bca.byc.model.api.BusinessDetailResponse;
import com.bca.byc.repository.BusinessRepository;
import com.bca.byc.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private BusinessRepository repository;

    @Autowired
    private BusinessDTOConverter converter;






    @Override
    public List<BusinessDetailResponse> findAllBusiness() {
        return null;
    }

    @Override
    public void saveBusiness(BusinessCreateRequest dto) throws Exception {

        Business business = converter.convertToCreateRequest(dto);

        repository.save(business);

    }

}
