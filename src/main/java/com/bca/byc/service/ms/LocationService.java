package com.bca.byc.service.ms;

import com.bca.byc.entity.Location;
import com.bca.byc.entity.User;
import com.bca.byc.model.cms.LocationCreateRequest;
import com.bca.byc.model.cms.LocationDetailResponse;
import com.bca.byc.model.cms.LocationUpdateRequest;

import java.util.List;

public interface LocationService {

    boolean existsById(Long id);

    LocationDetailResponse findLocationById(Long id);

    List<Location> findAllLocations();

    void createLocation(LocationCreateRequest dto);

    void updateLocation(Long locationId, LocationUpdateRequest dto);

    void deleteLocation(Long locationId);
}
