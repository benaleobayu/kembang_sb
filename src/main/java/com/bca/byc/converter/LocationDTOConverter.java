package com.bca.byc.converter;

import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.Location;
import com.bca.byc.model.LocationCreateUpdateRequest;
import com.bca.byc.model.LocationDetailResponse;
import com.bca.byc.model.LocationIndexResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class LocationDTOConverter {

    private ModelMapper modelMapper;

    public LocationIndexResponse convertToIndexResponse(Location data) {
        LocationIndexResponse dto = new LocationIndexResponse();
        dto.setName(data.getName());
        dto.setAddress(data.getAddress());
        dto.setOrders(data.getOrders());
        dto.setStatus(data.getIsActive());
        GlobalConverter.CmsIDTimeStampResponse(dto, data); // timestamp and id
        return dto;
    }

    // for get data
    public LocationDetailResponse convertToListResponse(Location data) {
        // mapping Entity with DTO Entity
        LocationDetailResponse dto = new LocationDetailResponse();
        dto.setName(data.getName());
        dto.setAddress(data.getAddress());
        dto.setOrders(data.getOrders());
        dto.setStatus(data.getIsActive());
        GlobalConverter.CmsIDTimeStampResponse(dto, data); // timestamp and id
        // return
        return dto;
    }

    // for create data
    public Location convertToCreateRequest(@Valid LocationCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        Location data = modelMapper.map(dto, Location.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Location data, @Valid LocationCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }


}
