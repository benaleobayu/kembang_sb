package com.bca.byc.service;

import com.bca.byc.model.apps.CommentCreateRequest;
import com.bca.byc.model.apps.CommentDetailResponse;
import jakarta.validation.Valid;
import com.bca.byc.exception.BadRequestException;

import java.util.List;

public interface CommentService {

    CommentDetailResponse findDataById(Long id) throws BadRequestException;

    List<CommentDetailResponse> findAllData();

    void saveData(@Valid CommentCreateRequest dto) throws BadRequestException;

    void updateData(Long id, @Valid CommentCreateRequest dto) throws BadRequestException;

    void deleteData(Long id) throws BadRequestException;
}
