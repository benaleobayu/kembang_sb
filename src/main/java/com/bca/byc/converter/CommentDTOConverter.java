package com.bca.byc.converter;

import com.bca.byc.entity.Comment;
import com.bca.byc.entity.Post;
import com.bca.byc.model.apps.CommentDetailResponse;
import com.bca.byc.model.apps.CommentCreateRequest;

import com.bca.byc.model.apps.ListCommentResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class CommentDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public ListCommentResponse convertToPageListResponse(Comment data) {
        // mapping Entity with DTO Entity
        ListCommentResponse dto = modelMapper.map(data, ListCommentResponse.class);
        // return
        return dto;
    }

    // for get data
    public CommentDetailResponse convertToListResponse(Comment data) {
        // mapping Entity with DTO Entity
        CommentDetailResponse dto = modelMapper.map(data, CommentDetailResponse.class);
        // return
        return dto;
    }



    // for create data
    public Comment convertToCreateRequest(Post postData, @Valid CommentCreateRequest dto) {
        // mapping DTO Entity with Entity
        Comment data = modelMapper.map(dto, Comment.class);
        data.setPost(postData);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Comment data, @Valid CommentCreateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}

