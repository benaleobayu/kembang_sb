package com.bca.byc.converter;

import com.bca.byc.entity.Faq;
import com.bca.byc.model.FaqCreateUpdateRequest;
import com.bca.byc.model.FaqDetailResponse;

import com.bca.byc.model.FaqIndexResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class FaqDTOConverter {

    private ModelMapper modelMapper;

    // for get data index
    public FaqIndexResponse convertToIndexResponse(Faq data) {
        // mapping Entity with DTO Entity
        FaqIndexResponse dto = modelMapper.map(data, FaqIndexResponse.class);
        // return
        return dto;
    }

     // for get data detail
    public FaqDetailResponse convertToDetailResponse(Faq data) {
        // mapping Entity with DTO Entity
        FaqDetailResponse dto = modelMapper.map(data, FaqDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public Faq convertToCreateRequest(@Valid FaqCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        Faq data = modelMapper.map(dto, Faq.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Faq data, @Valid FaqCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}