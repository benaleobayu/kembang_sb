package com.kembang.converter;

import com.kembang.entity.OrderRoute;
import com.kembang.model.OrderRouteCreateUpdateRequest;
import com.kembang.model.OrderRouteDetailResponse;
import com.kembang.model.OrderRouteIndexResponse;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class OrderRouteDTOConverter {

    private ModelMapper modelMapper;

    //for get index data
    public OrderRouteIndexResponse convertToIndexResponse(OrderRoute data) {
        // mapping Entity with DTO Entity
        OrderRouteIndexResponse dto = modelMapper.map(data, OrderRouteIndexResponse.class);
        // return
        return dto;
    }

    // for get detail data
    public OrderRouteDetailResponse convertToDetailResponse(OrderRoute data) {
        // mapping Entity with DTO Entity
        OrderRouteDetailResponse dto = modelMapper.map(data, OrderRouteDetailResponse.class);
        // return
        return dto;
    }

    // for create data
    public OrderRoute convertToCreateRequest(@Valid OrderRouteCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        OrderRoute data = modelMapper.map(dto, OrderRoute.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(OrderRoute data, @Valid OrderRouteCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
