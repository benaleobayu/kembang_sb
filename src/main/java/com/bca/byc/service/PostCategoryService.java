package com.bca.byc.service;

import com.bca.byc.model.PostCategoryCreateUpdateRequest;
import com.bca.byc.model.PostCategoryDetailResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import jakarta.validation.Valid;
import com.bca.byc.exception.BadRequestException;

import java.util.List;

public interface PostCategoryService {

    PostCategoryDetailResponse findDataById(Long id) throws BadRequestException;

    List<PostCategoryDetailResponse> findAllData();

    void saveData(@Valid PostCategoryCreateUpdateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid PostCategoryCreateUpdateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;

    ResultPageResponseDTO<PostCategoryDetailResponse> listData(Integer pages, Integer limit, String sortBy, String direction, String keyword);
}
