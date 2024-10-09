package com.bca.byc.service.cms.impl;

import com.bca.byc.converter.ChanelDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.entity.Chanel;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ChanelCreateUpdateRequest;
import com.bca.byc.model.ChanelDetailResponse;
import com.bca.byc.model.ChanelIndexResponse;
import com.bca.byc.repository.ChanelRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.cms.ChanelService;
import com.bca.byc.util.PaginationUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChanelServiceImpl implements ChanelService {

    private ChanelRepository repository;
    private ChanelDTOConverter converter;

    @Override
    public ResultPageResponseDTO<ChanelIndexResponse> listDataChanelIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<Chanel> pageResult = repository.findByNameLikeIgnoreCase(keyword, pageable);
        List<ChanelIndexResponse> dtos = pageResult.stream().map((c) -> {
            ChanelIndexResponse dto = converter.convertToIndexResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ChanelDetailResponse findDataById(String id) throws BadRequestException {
        Chanel data = HandlerRepository.getEntityBySecureId(id, repository, "Chanel not found");

        return converter.convertToDetailResponse(data);
    }

    @Override
    public void saveData(@Valid ChanelCreateUpdateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        Chanel data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(String id, ChanelCreateUpdateRequest dto) throws BadRequestException {
        Chanel data = HandlerRepository.getEntityBySecureId(id, repository, "Chanel not found");

        // update
        converter.convertToUpdateRequest(data, dto);

        // update the updated_at
        data.setUpdatedAt(LocalDateTime.now());

        // save
        repository.save(data);
    }

    @Override
    public void deleteData(String id) throws BadRequestException {
        Chanel data = HandlerRepository.getEntityBySecureId(id, repository, "Chanel not found");
        // delete data
        if (!repository.existsById(data.getId())) {
            throw new BadRequestException("Chanel not found");
        } else {
            repository.deleteById(data.getId());
        }
    }
}
