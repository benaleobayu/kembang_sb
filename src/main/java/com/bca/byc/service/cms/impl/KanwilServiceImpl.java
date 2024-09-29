package com.bca.byc.service.cms.impl;

import com.bca.byc.converter.KanwilDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.entity.Kanwil;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.KanwilCreateUpdateRequest;
import com.bca.byc.model.KanwilDetailResponse;
import com.bca.byc.model.KanwilListResponse;
import com.bca.byc.repository.KanwilRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.cms.KanwilService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.bca.byc.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class KanwilServiceImpl implements KanwilService {

    private KanwilRepository repository;
    private KanwilDTOConverter converter;

    private static String notFoundMessage = "Kanwil not found";

    @Override
        public ResultPageResponseDTO<KanwilListResponse> listDataKanwil(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
            keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
            Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
            Pageable pageable = PageRequest.of(pages, limit, sort);
            Page<Kanwil> pageResult = repository.findDataByKeyword(keyword, pageable);
            List<KanwilListResponse> dtos = pageResult.stream().map((c) -> {
                KanwilListResponse dto = converter.convertToListResponse(c);
                return dto;
            }).collect(Collectors.toList());

            return PageCreateReturn.create(pageResult, dtos);
        }

    @Override
    public KanwilDetailResponse findDataBySecureId(String id) throws BadRequestException {
        Kanwil data = HandlerRepository.getEntityBySecureId(id, repository, notFoundMessage);

        return converter.convertToDetailResponse(data);
    }


    @Override
    @Transactional
    public void saveData(@Valid KanwilCreateUpdateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        Kanwil data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    @Transactional
    public void updateData(String id, KanwilCreateUpdateRequest dto) throws BadRequestException {
        // check exist and get
        Kanwil data = HandlerRepository.getEntityBySecureId(id, repository, notFoundMessage);

        // update
        converter.convertToUpdateRequest(data, dto);

        // update the updated_at
        data.setUpdatedAt(LocalDateTime.now());

        // save
        repository.save(data);
    }

    @Override
    @Transactional
    public void deleteData(String id) throws BadRequestException {
        Kanwil kanwil = HandlerRepository.getEntityBySecureId(id, repository, notFoundMessage);
        Long KanwilId = kanwil.getId();
        // delete data
        if (!repository.existsById(KanwilId)) {
            throw new BadRequestException("Kanwil not found");
        } else {
            repository.deleteById(KanwilId);
        }
    }
}
