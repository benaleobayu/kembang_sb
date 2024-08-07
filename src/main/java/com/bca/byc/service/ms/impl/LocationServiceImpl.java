package com.bca.byc.service.ms.impl;

import com.bca.byc.convert.cms.LocationDTOConverter;
import com.bca.byc.entity.Location;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.LocationCreateRequest;
import com.bca.byc.model.cms.LocationDetailResponse;
import com.bca.byc.model.cms.LocationUpdateRequest;
import com.bca.byc.repository.cms.LocationRepository;
import com.bca.byc.service.ms.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("locationService")
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository repository;

    @Autowired
    private LocationDTOConverter locationDTOConverter;


    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public LocationDetailResponse findLocationById(Long id) {
        Location location = repository.findById(id)
                .orElseThrow(()-> new BadRequestException("invalid.locationId"));

        LocationDetailResponse dto = locationDTOConverter.convertDetailToDTO(location);

        return dto;
    }

    @Override
    public List<Location> findAllLocations() {
        List<Location> locations = repository.findAll();

        return  locations;
    }

    @Override
    public void createLocation(LocationCreateRequest dto) {
        Location location = locationDTOConverter.convertToCreate(dto);

        // save
        repository.save(location);
    }

    @Override
    public void updateLocation(Long locationId, LocationUpdateRequest dto) {

    }

    @Override
    public void deleteLocation(Long locationId) {

    }
}
