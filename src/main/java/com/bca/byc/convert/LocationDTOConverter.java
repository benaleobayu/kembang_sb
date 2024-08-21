package com.bca.byc.convert;

import com.bca.byc.entity.Location;
import com.bca.byc.model.cms.LocationModelDTO;
import com.bca.byc.util.Formatter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class LocationDTOConverter {

    private ModelMapper modelMapper;

    // for get data
    public LocationModelDTO.LocationDetailResponse convertToListResponse(Location data) {
        // mapping Entity with DTO Entity
        LocationModelDTO.LocationDetailResponse dto = modelMapper.map(data, LocationModelDTO.LocationDetailResponse.class);
        // Use DataFormatter here
        dto.setDescription(Formatter.formatDescription(data.getDescription()));
        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));

        // return
        return dto;
    }

    // for create data
    public Location convertToCreateRequest(@Valid LocationModelDTO.LocationCreateRequest dto) {
        // mapping DTO Entity with Entity
        Location data = modelMapper.map(dto, Location.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Location data, @Valid LocationModelDTO.LocationUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
