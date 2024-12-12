package com.kembang.service.cms;

import com.kembang.model.LocationCreateUpdateRequest;
import com.kembang.model.LocationDetailResponse;
import com.kembang.model.LocationIndexResponse;
import com.kembang.response.ResultPageResponseDTO;
import jakarta.validation.Valid;
import com.kembang.exception.BadRequestException;

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
