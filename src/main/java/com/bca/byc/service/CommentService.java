package com.bca.byc.service;

import com.bca.byc.exception.ResourceNotFoundException;
import com.bca.byc.model.apps.CommentCreateUpdateRequest;
import com.bca.byc.model.apps.CommentDetailResponse;
import jakarta.validation.Valid;
import com.bca.byc.exception.BadRequestException;

public interface CommentService {

    Object listDataComment(Integer pages, Integer limit, String sortBy, String direction, String keyword, String postId);

    CommentDetailResponse findDataById(String postId, String commentId) throws BadRequestException;

    void saveData(String postId, @Valid CommentCreateUpdateRequest dto, String email) throws BadRequestException;

    void updateData(String postId, String commentId, @Valid CommentCreateUpdateRequest dto, String email) throws BadRequestException;

    void deleteData(String postId, String commentId, String email) throws ResourceNotFoundException;
}
