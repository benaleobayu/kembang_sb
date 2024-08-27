package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.InterestModelDTO;

import jakarta.validation.Valid;
import java.util.List;

public interface InterestService {

    InterestModelDTO.InterestDetailResponse findDataById(Long id) throws BadRequestException;

    List<InterestModelDTO.InterestDetailResponse> findAllData();

    void saveData(@Valid InterestModelDTO.InterestCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid InterestModelDTO.InterestUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
