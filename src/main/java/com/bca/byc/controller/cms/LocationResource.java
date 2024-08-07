package com.bca.byc.controller.cms;

import com.bca.byc.convert.cms.LocationDTOConverter;
import com.bca.byc.entity.Location;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.LocationCreateRequest;
import com.bca.byc.repository.cms.LocationRepository;
import com.bca.byc.response.ApiListResponse;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.service.ms.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationResource {

    @Autowired
    private LocationRepository repository;

    @Autowired
    private LocationDTOConverter locationDTOConverter;

    @Autowired
    private LocationService locationService;

    @GetMapping
    public List<Location> getAllLocations() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<ApiResponse> saveLocation(@RequestBody LocationCreateRequest dto) {

        Location location = locationDTOConverter.convertToCreate(dto);
        repository.save(location);

        return ResponseEntity.ok(new ApiResponse(true, "Location created successfully."));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiListResponse> findLocationById(@PathVariable("userId") Long userId) {

        if (!locationService.existsById(userId)) {
            throw new BadRequestException("invalid.locationId");
        }

        return ResponseEntity.ok(new ApiListResponse(
                "success",
                "Location found successfully.",
                locationService.findLocationById(userId)));
    }


}
