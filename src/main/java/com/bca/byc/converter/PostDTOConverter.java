package com.bca.byc.converter;

import com.bca.byc.entity.Post;
import com.bca.byc.model.PostCreateUpdateRequest;
import com.bca.byc.model.PostDetailResponse;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PostDTOConverter {

    @Value("${upload.dir}")
    private String UPLOAD_DIR;


    private final ModelMapper modelMapper;

    // for get data
    public PostDetailResponse convertToListResponse(Post data) {
        // mapping Entity with DTO Entity
        PostDetailResponse dto = modelMapper.map(data, PostDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public Post convertToCreateRequest(@Valid PostCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        Post data = modelMapper.map(dto, Post.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Post data, @Valid PostCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }


}
