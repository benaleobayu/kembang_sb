package com.bca.byc.service;

import com.bca.byc.model.api.InterestCreateRequest;
import com.bca.byc.model.api.InterestDetailResponse;
import com.bca.byc.model.api.InterestUpdateRequest;

import javax.validation.Valid;
import java.util.List;

public interface InterestService {

//    boolean existsById(Long id);

    InterestDetailResponse findDataById(Long id);

    List<InterestDetailResponse> getAllData();

    void saveData(InterestCreateRequest dto) throws Exception;

    void updateData(Long id, @Valid InterestUpdateRequest dto);

    void deleteData(Long id);
}
