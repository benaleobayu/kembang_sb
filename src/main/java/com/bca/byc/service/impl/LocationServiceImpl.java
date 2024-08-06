package com.bca.byc.service.impl;

import com.bca.byc.entity.Location;
import com.bca.byc.model.LocationCreate;
import com.bca.byc.repository.LocationRepository;
import com.bca.byc.service.LocationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service("locationService")
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository repository;

    @Override
    public void createLocation(LocationCreate dto) {
        Location location = new Location();
        location.setName(dto.getName());
        location.setDescription(dto.getDescription());
        location.setOrder(dto.getOrder());
        location.setStatus(dto.getStatus());
        repository.save(location);
    }
}
