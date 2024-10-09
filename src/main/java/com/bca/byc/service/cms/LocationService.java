package com.bca.byc.service.cms;

import com.bca.byc.model.LocationCreateUpdateRequest;
import com.bca.byc.model.LocationDetailResponse;
import com.bca.byc.model.LocationIndexResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;
import com.bca.byc.exception.BadRequestException;

import java.util.List;

public interface LocationService {

    // --- public ---
    List<LocationDetailResponse> findAllData();
    // --- public ---

    ResultPageResponseDTO<LocationIndexResponse> listDataLocation(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    LocationDetailResponse findDataById(String id) throws BadRequestException;

    void saveData(@Valid LocationCreateUpdateRequest dto) throws BadRequestException;

    void updateData(String id, @Valid LocationCreateUpdateRequest dto) throws BadRequestException;

    void deleteData(String id) throws BadRequestException;
}
