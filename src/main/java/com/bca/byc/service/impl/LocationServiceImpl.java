package com.bca.byc.service.impl;

import com.bca.byc.converter.LocationDTOConverter;
import com.bca.byc.entity.Location;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.LocationCreateRequest;
import com.bca.byc.model.LocationDetailResponse;
import com.bca.byc.model.LocationUpdateRequest;
import com.bca.byc.repository.LocationRepository;
import com.bca.byc.service.MsLocationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements MsLocationService {

    private LocationRepository repository;
    private LocationDTOConverter converter;

    @Override
    public LocationDetailResponse findDataById(Long id) {
        Location data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Location not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<LocationDetailResponse> findAllData() {
        // Get the list
        List<Location> datas = repository.findAllAndOrderByName();


        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid LocationCreateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        Location data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, LocationUpdateRequest dto) throws BadRequestException {
        // check exist and get
        Location data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID Location ID"));

        // update
        converter.convertToUpdateRequest(data, dto);

        // update the updated_at
        data.setUpdatedAt(LocalDateTime.now());

        // save
        repository.save(data);
    }

    @Override
    public void deleteData(Long id) throws BadRequestException {
        // delete data
        if (!repository.existsById(id)) {
            throw new BadRequestException("Location not found");
        } else {
            repository.deleteById(id);
        }
    }

    @Override
    public boolean deleteDataById(Long id) {
        if (!repository.existsById(id)) {
            return false;
        } else {
            repository.deleteById(id);
            return true;
        }
    }
}
