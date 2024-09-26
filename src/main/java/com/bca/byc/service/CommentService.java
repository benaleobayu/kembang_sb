package com.bca.byc.service;

import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.apps.CommentCreateRequest;
import com.bca.byc.model.apps.CommentDetailResponse;
import jakarta.validation.Valid;
import com.bca.byc.exception.BadRequestException;

public interface CommentService {

    Object listDataComment(Integer pages, Integer limit, String sortBy, String direction, String keyword, String postId);

    CommentDetailResponse findDataById(String postId, Long id) throws BadRequestException;

    void saveData(String postId, @Valid CommentCreateRequest dto, String email) throws BadRequestException;

    void updateData(String postId, Long id, @Valid CommentCreateRequest dto, String email) throws BadRequestException;

    void deleteData(String postId, Long id, String email) throws ResourceNotFoundException;
}
