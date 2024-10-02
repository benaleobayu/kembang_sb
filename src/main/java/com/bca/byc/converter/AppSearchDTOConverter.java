package com.bca.byc.converter;

import com.bca.byc.entity.Post;
import com.bca.byc.model.AppSearchDetailResponse;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AppSearchDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public AppSearchDetailResponse convertToListResponse(Post data) {
        // mapping Entity with DTO Entity
        AppSearchDetailResponse dto = new AppSearchDetailResponse();
        // return
        return dto;
    }

}

