package com.bca.byc.service.cms.impl;

import com.bca.byc.converter.LocationDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Location;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.LocationCreateUpdateRequest;
import com.bca.byc.model.LocationDetailResponse;
import com.bca.byc.model.LocationIndexResponse;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.LocationRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.cms.LocationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final AppAdminRepository adminRepository;

    private LocationRepository repository;
    private LocationDTOConverter converter;

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
    public ResultPageResponseDTO<LocationIndexResponse> listDataLocation(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<Location> pageResult = repository.findByNameLikeIgnoreCase(set.keyword(), set.pageable());
        List<LocationIndexResponse> dtos = pageResult.stream().map((c) -> {
            LocationIndexResponse dto = converter.convertToIndexResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public LocationDetailResponse findDataById(String id) throws BadRequestException {
        Location data = HandlerRepository.getEntityBySecureId(id, repository, "Location not found");

        return converter.convertToListResponse(data);
    }

    @Override
    public void saveData(@Valid LocationCreateUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        // set entity to add with model mapper
        Location data = converter.convertToCreateRequest(dto);
        data.setCreatedBy(admin);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(String id, LocationCreateUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        Location data = HandlerRepository.getEntityBySecureId(id, repository, "Location not found");

        // update
        converter.convertToUpdateRequest(data, dto);
        // update the updated_at
        data.setUpdatedBy(admin);
        // save
        repository.save(data);
    }

    @Override
    public void deleteData(String id) throws BadRequestException {
        Location data = HandlerRepository.getEntityBySecureId(id, repository, "Location not found");
        // delete data
        if (!repository.existsById(data.getId())) {
            throw new BadRequestException("Location not found");
        } else {
            repository.deleteById(data.getId());
        }
    }
}
