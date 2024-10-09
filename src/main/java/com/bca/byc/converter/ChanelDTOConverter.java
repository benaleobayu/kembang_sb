package com.bca.byc.converter;

import com.bca.byc.entity.Channel;
import com.bca.byc.model.ChanelCreateUpdateRequest;
import com.bca.byc.model.ChanelDetailResponse;

import com.bca.byc.model.ChanelIndexResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class ChanelDTOConverter {

    private ModelMapper modelMapper;

    // for get data index
    public ChanelIndexResponse convertToIndexResponse(Channel data) {
        // mapping Entity with DTO Entity
        ChanelIndexResponse dto = modelMapper.map(data, ChanelIndexResponse.class);
        // return
        return dto;
    }
 // for get data
    public ChanelDetailResponse convertToDetailResponse(Channel data) {
        // mapping Entity with DTO Entity
        ChanelDetailResponse dto = modelMapper.map(data, ChanelDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public Channel convertToCreateRequest(@Valid ChanelCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        Channel data = modelMapper.map(dto, Channel.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Channel data, @Valid ChanelCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}

