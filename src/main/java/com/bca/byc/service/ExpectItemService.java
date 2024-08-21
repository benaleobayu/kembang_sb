package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ExpectItemModelDTO;

import javax.validation.Valid;
import java.util.List;

public interface ExpectItemService {

    ExpectItemModelDTO.ExpectItemDetailResponse findDataById(Long id) throws BadRequestException;

    List<ExpectItemModelDTO.ExpectItemDetailResponse> findAllData();

    void saveData(@Valid ExpectItemModelDTO.ExpectItemCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid ExpectItemModelDTO.ExpectItemUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
