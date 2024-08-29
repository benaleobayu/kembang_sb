package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ExpectItemCreateRequest;
import com.bca.byc.model.ExpectItemDetailResponse;
import com.bca.byc.model.ExpectItemUpdateRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface ExpectItemService {

    ExpectItemDetailResponse findDataById(Long id) throws BadRequestException;

    List<ExpectItemDetailResponse> findAllData();

    void saveData(@Valid ExpectItemCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid ExpectItemUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
