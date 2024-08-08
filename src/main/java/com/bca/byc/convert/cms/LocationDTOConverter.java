package com.bca.byc.convert.cms;

import com.bca.byc.entity.Location;
import com.bca.byc.model.cms.LocationCreateRequest;
import com.bca.byc.model.cms.LocationDetailResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LocationDTOConverter {

    @Autowired
    private ModelMapper modelMapper;

    public LocationDetailResponse convertToListResponse(Location location){

        LocationDetailResponse dto = modelMapper.map(location, LocationDetailResponse.class);

        return dto;
    }

    public void convertToCreateRequest(Location entity,@Valid LocationCreateRequest dto){

        modelMapper.map(dto, entity);

        entity.setUpdatedAt(LocalDateTime.now());
    }
}
