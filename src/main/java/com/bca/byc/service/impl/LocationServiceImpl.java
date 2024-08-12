package com.bca.byc.service.impl;

import com.bca.byc.convert.LocationDTOConverter;
import com.bca.byc.entity.Location;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.LocationModelDTO;
import com.bca.byc.repository.LocationRepository;
import com.bca.byc.service.LocationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {

    private LocationRepository repository;
    private LocationDTOConverter converter;

    @Override
    public LocationModelDTO.DetailResponse findDataById(Long id) throws BadRequestException {
        Location data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Location not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<LocationModelDTO.DetailResponse> findAllData() {
        // Get the list
        List<Location> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid LocationModelDTO.CreateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        Location data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, LocationModelDTO.UpdateRequest dto) throws BadRequestException {
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
}
