package com.bca.byc.convert.cms;

import com.bca.byc.entity.Location;
import com.bca.byc.model.cms.LocationCreateRequest;
import com.bca.byc.model.cms.LocationDetailResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocationDTOConverter {

    @Autowired
    private ModelMapper modelMapper;

    public LocationDetailResponse convertDetailToDTO(Location location){
        LocationDetailResponse dto = modelMapper.map(location, LocationDetailResponse.class);
        return dto;
    }

    public Location convertToCreate(LocationCreateRequest dto){
        Location location = modelMapper.map(dto, Location.class);

        return location;
    }
}
