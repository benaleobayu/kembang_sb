package com.kembang.service.cms.impl;

import com.kembang.converter.LocationDTOConverter;
import com.kembang.converter.dictionary.PageCreateReturnApps;
import com.kembang.converter.parsing.GlobalConverter;
import com.kembang.entity.AppAdmin;
import com.kembang.entity.Location;
import com.kembang.exception.BadRequestException;
import com.kembang.model.LocationCreateUpdateRequest;
import com.kembang.model.LocationDetailResponse;
import com.kembang.model.LocationIndexResponse;
import com.kembang.model.search.ListOfFilterPagination;
import com.kembang.model.search.SavedKeywordAndPageable;
import com.kembang.repository.LocationRepository;
import com.kembang.repository.auth.AppAdminRepository;
import com.kembang.repository.handler.HandlerRepository;
import com.kembang.response.ResultPageResponseDTO;
import com.kembang.service.cms.LocationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.kembang.converter.parsing.TreeGetEntityProjection.getParsingLocationByProjection;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final AppAdminRepository adminRepository;

    private LocationRepository repository;
    private LocationDTOConverter converter;

    @Override
    public ResultPageResponseDTO<LocationIndexResponse> listDataLocation(Integer pages, Integer limit, String sortBy, String direction, String keyword) {

        Page<Location> firstResult = repository.findByNameLikeIgnoreCase(null,null);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, firstResult);

        Page<Location> pageResult = repository.findByNameLikeIgnoreCase(set.keyword(), set.pageable());
        List<LocationIndexResponse> dtos = pageResult.stream().map((c) -> {
            LocationIndexResponse dto = converter.convertToIndexResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, dtos);
    }

    @Override
    public LocationDetailResponse findDataById(String id) throws BadRequestException {
        Location data = getParsingLocationByProjection(id, repository);

        return converter.convertToListResponse(data);
    }

    @Override
    public void saveData(@Valid LocationCreateUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        // set entity to add with model mapper
        Location data = converter.convertToCreateRequest(dto);
        data.setCreatedBy(admin.getId());
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
        data.setUpdatedBy(admin.getId());
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
