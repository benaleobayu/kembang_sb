package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.LocationCreateRequest;
import com.bca.byc.model.LocationDetailResponse;
import com.bca.byc.model.LocationUpdateRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface MsLocationService {

    LocationDetailResponse findDataById(Long id);

    List<LocationDetailResponse> findAllData();

    void saveData(@Valid LocationCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid LocationUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;


    // extras cms

    boolean deleteDataById(Long id);
}
