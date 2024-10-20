package com.bca.byc.service.impl;

import com.bca.byc.converter.InputAttributeDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.*;
import com.bca.byc.enums.AdminType;
import com.bca.byc.enums.UserType;
import com.bca.byc.model.AttributeNameResponse;
import com.bca.byc.entity.impl.AttrIdentificable;
import com.bca.byc.model.attribute.AttributeResponse;
import com.bca.byc.model.projection.CastSecureIdAndNameProjection;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.*;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.InputAttributeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InputAttributeServiceImpl implements InputAttributeService {

    private final BusinessCategoryRepository businessCategoryRepository;
    private final ExpectItemRepository expectItemRepository;
    private final BranchRepository branchRepository;
    private final KanwilRepository kanwilRepository;
    private final LocationRepository locationRepository;
    private final ChannelRepository channelRepository;
    private final RoleRepository roleRepository;

    private final InputAttributeDTOConverter converter;

    @Override
    public ResultPageResponseDTO<AttributeNameResponse> listDataSubCategoryBusiness(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<BusinessCategory> pageResult = businessCategoryRepository.findIdAndName(set.keyword(), set.pageable());
        List<AttributeNameResponse> dtos = pageResult.stream()
                .map(this::convertToListAttributeOnName)
                .collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ResultPageResponseDTO<AttributeNameResponse> listDataSubCategoryExpect(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<ExpectItem> pageResult = expectItemRepository.findIdAndName(set.keyword(), set.pageable());
        List<AttributeNameResponse> dtos = pageResult.stream()
                .map(this::convertToListAttributeOnName)
                .collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ResultPageResponseDTO<AttributeResponse<Long>> listDataBranch(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);
        Page<Branch> pageResult = branchRepository.findIdAndName(set.keyword(), set.pageable());
        List<AttributeResponse> dtos = pageResult.stream()
                .map(this::convertToListAttribute)
                .collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ResultPageResponseDTO<AttributeResponse<Long>> listDataKanwil(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);
        Page<Kanwil> pageResult = kanwilRepository.findIdAndName(set.keyword(), set.pageable());
        List<AttributeResponse> dtos = pageResult.stream()
                .map(this::convertToListAttribute)
                .collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ResultPageResponseDTO<AttributeResponse<Long>> listDataLocation(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);
        Page<Location> pageResult = locationRepository.findIdAndName(set.keyword(), set.pageable());
        List<AttributeResponse> dtos = pageResult.stream()
                .map(this::convertToListAttribute)
                .collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ResultPageResponseDTO<AttributeResponse<Long>> listDataChannel(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);
        Page<CastSecureIdAndNameProjection> pageResult = channelRepository.findIdAndName(set.keyword(), set.pageable());
        List<AttributeResponse> dtos = pageResult.stream()
                .map(this::convertToListAttribute)
                .collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    public ResultPageResponseDTO<AttributeResponse<String>> RoleList(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);
        Page<CastSecureIdAndNameProjection> pageResult = roleRepository.findSecureIdAndName(set.keyword(), set.pageable());
        List<AttributeResponse> dtos = pageResult.stream()
                .map(this::convertToListAttribute)
                .collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public List<Map<String, List<?>>> listReportDetailOf(String detailOf) {
        List<Map<String, List<?>>> attributes = new ArrayList<>();
        // Data UserTypes
        List<AttributeResponse<String>> listUserTypes = Arrays.asList(
                new AttributeResponse<>( "POST" , "Solitaire")
        );
        Map<String, List<?>> userTypeMap = new HashMap<>();
        userTypeMap.put("segmentation", listUserTypes);
        attributes.add(userTypeMap);

        return attributes;
    }

    @Override
    public List<Map<String, List<?>>> AdminType(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        List<Map<String, List<?>>> attributes = new ArrayList<>();
        // Data UserTypes
        List<AttributeResponse<String>> listAdminTypes = Arrays.asList(
                new AttributeResponse<>(AdminType.GENERAL.name(), "General"),
                new AttributeResponse<>(AdminType.OPERATIONAL.name(), "Operational"),
                new AttributeResponse<>(AdminType.SUPERVISOR.name(), "Supervisor"),
                new AttributeResponse<>(AdminType.CONTENT.name(), "Content")
        );
        Map<String, List<?>> userTypeMap = new HashMap<>();
        userTypeMap.put("admin_type", listAdminTypes);
        attributes.add(userTypeMap);

        return attributes;
    }


    // ------
    public AttributeNameResponse convertToListAttributeOnName(AttrIdentificable dto) {
        AttributeNameResponse response = new AttributeNameResponse();
        response.setName(dto.getName());
        return response;
    }

    public AttributeResponse convertToListAttribute(AttrIdentificable dto) {
        AttributeResponse<String> response = new AttributeResponse<>();
        response.setId(dto.getSecureId());
        response.setName(dto.getName());
        return response;
    }




}
