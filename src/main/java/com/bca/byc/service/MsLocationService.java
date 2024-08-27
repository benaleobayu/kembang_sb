package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.LocationModelDTO;

import jakarta.validation.Valid;
import java.util.List;

public interface MsLocationService {

    LocationModelDTO.LocationDetailResponse findDataById(Long id) ;

    List<LocationModelDTO.LocationDetailResponse> findAllData();

    void saveData(@Valid LocationModelDTO.LocationCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid LocationModelDTO.LocationUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;


    // extras cms

    boolean deleteDataById(Long id);
}
