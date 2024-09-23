package com.bca.byc.service;

import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.data.TagCreateUpdateRequest;
import com.bca.byc.model.data.TagDetailResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface TagService {

    ResultPageResponseDTO<TagDetailResponse> listDataTag(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    ResultPageResponseDTO<TagDetailResponse> listDataTagApps(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    TagDetailResponse findDataById(Long id) throws BadRequestException;

    void saveData(@Valid TagCreateUpdateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid TagCreateUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;

}
