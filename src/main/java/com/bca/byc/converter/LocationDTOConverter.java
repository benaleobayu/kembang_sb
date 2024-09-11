package com.bca.byc.converter;

import com.bca.byc.entity.Location;
import com.bca.byc.model.LocationCreateRequest;
import com.bca.byc.model.LocationDetailResponse;
import com.bca.byc.model.LocationUpdateRequest;
import com.bca.byc.util.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class LocationDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public LocationDetailResponse convertToListResponse(Location data) {
        // mapping Entity with DTO Entity
        LocationDetailResponse dto = modelMapper.map(data, LocationDetailResponse.class);
        // Use DataFormatter here
        dto.setDescription(Formatter.formatDescription(data.getDescription()));
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));

        // return
        return dto;
    }

    // for create data
    public Location convertToCreateRequest(@Valid LocationCreateRequest dto) {
        // mapping DTO Entity with Entity
        Location data = modelMapper.map(dto, Location.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Location data, @Valid LocationUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}