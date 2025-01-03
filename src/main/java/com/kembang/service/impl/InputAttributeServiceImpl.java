package com.kembang.service.impl;

import com.kembang.converter.dictionary.PageCreateReturnApps;
import com.kembang.converter.parsing.GlobalConverter;
import com.kembang.entity.Location;
import com.kembang.entity.impl.AttrIdentificable;
import com.kembang.model.AttributeNameResponse;
import com.kembang.model.attribute.AttributeResponse;
import com.kembang.model.projection.CastSecureIdAndNameProjection;
import com.kembang.model.search.SavedKeywordAndPageable;
import com.kembang.repository.LocationRepository;
import com.kembang.repository.RoleRepository;
import com.kembang.response.ResultPageResponseDTO;
import com.kembang.service.InputAttributeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InputAttributeServiceImpl implements InputAttributeService {

    private final LocationRepository locationRepository;
    private final RoleRepository roleRepository;

    @Override
    public ResultPageResponseDTO<AttributeResponse<Long>> listDataLocation(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword);
        Page<Location> firstResult = locationRepository.findIdAndName(set.keyword(), set.pageable());
        Pageable pageable = GlobalConverter.oldSetPageable(pages, limit, sortBy, direction, firstResult, null);

        Page<Location> pageResult = locationRepository.findIdAndName(set.keyword(), pageable);
        List<AttributeResponse> dtos = pageResult.stream()
                .map(this::convertToListAttribute)
                .collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, dtos);
    }

    public ResultPageResponseDTO<AttributeResponse<String>> RoleList(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        // pageable
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword);
        Page<CastSecureIdAndNameProjection> firstResult = roleRepository.findSecureIdAndName(set.keyword(), set.pageable());
        Pageable pageable = GlobalConverter.oldSetPageable(pages, limit, sortBy, direction, firstResult, null);

        // get data and stream
        Page<CastSecureIdAndNameProjection> pageResult = roleRepository.findSecureIdAndName(set.keyword(), pageable);
        List<AttributeResponse> dtos = pageResult.stream()
                .map(this::convertToListAttribute)
                .collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, dtos);
    }

    // ------
    public AttributeNameResponse convertToListAttributeOnName(AttrIdentificable dto) {
        AttributeNameResponse response = new AttributeNameResponse();
        response.setName(dto.getName());
        return response;
    }

    public AttributeResponse<String> convertToListAttribute(AttrIdentificable dto) {
        AttributeResponse<String> response = new AttributeResponse<>();
        response.setId(dto.getSecureId());
        response.setName(dto.getName());
        return response;
    }


}
