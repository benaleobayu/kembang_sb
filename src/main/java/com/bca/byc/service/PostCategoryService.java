package com.bca.byc.service;

import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.PostCategoryCreateUpdateRequest;
import com.bca.byc.model.PostCategoryDetailResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;
import com.bca.byc.exception.BadRequestException;

import java.util.List;

public interface PostCategoryService {

    ResultPageResponseDTO<PostCategoryDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    PostCategoryDetailResponse findDataById(Long id) throws ResourceNotFoundException;

    List<PostCategoryDetailResponse> findAllData() throws ResourceNotFoundException;

    void saveData(@Valid PostCategoryCreateUpdateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid PostCategoryCreateUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
