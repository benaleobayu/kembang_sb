package com.kembang.converter;

import com.kembang.converter.parsing.GlobalConverter;
import com.kembang.entity.Location;
import com.kembang.model.LocationCreateUpdateRequest;
import com.kembang.model.LocationDetailResponse;
import com.kembang.model.LocationIndexResponse;
import com.kembang.repository.auth.AppAdminRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class LocationDTOConverter {
    private final AppAdminRepository adminRepository;

    private ModelMapper modelMapper;

    public LocationIndexResponse convertToIndexResponse(Location data) {
        LocationIndexResponse dto = new LocationIndexResponse();
        dto.setName(data.getName());
        dto.setProvince(data.getProvince());
        dto.setOrders(data.getOrders());
        dto.setStatus(data.getIsActive());
        GlobalConverter.CmsIDTimeStampResponseAndId(dto, data, adminRepository); // timestamp and id
        return dto;
    }

    // for get data
    public LocationDetailResponse convertToListResponse(Location data) {
        // mapping Entity with DTO Entity
        LocationDetailResponse dto = new LocationDetailResponse();
        dto.setName(data.getName());
        dto.setProvince(data.getProvince());
        dto.setOrders(data.getOrders());
        dto.setStatus(data.getIsActive());
        GlobalConverter.CmsIDTimeStampResponseAndId(dto, data, adminRepository); // timestamp and id
        // return
        return dto;
    }

    // for create data
    public Location convertToCreateRequest(@Valid LocationCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        Location data = modelMapper.map(dto, Location.class);
        data.setIsActive(dto.getStatus());
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(Location data, @Valid LocationCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        data.setIsActive(dto.getStatus());
        data.setUpdatedAt(LocalDateTime.now());
    }


}
