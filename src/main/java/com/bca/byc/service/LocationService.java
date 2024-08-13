package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.LocationModelDTO;

import javax.validation.Valid;
import java.util.List;

public interface LocationService {

    LocationModelDTO.DetailResponse findDataById(Long id) ;

    List<LocationModelDTO.DetailResponse> findAllData();

    void saveData(@Valid LocationModelDTO.CreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid LocationModelDTO.UpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;


    // extras cms

    boolean deleteDataById(Long id);
}
