package com.bca.byc.service.impl;


import com.bca.byc.convert.ContentDTOConverter;
import com.bca.byc.entity.Content;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.ContentModelDTO;
import com.bca.byc.repository.ContentRepository;
import com.bca.byc.service.ContentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContentServiceImpl implements ContentService {

    private ContentRepository repository;
    private ContentDTOConverter converter;

    @Override
    public ContentModelDTO.ContentDetailResponse findDataById(Long id) throws BadRequestException {
        Content data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Content not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<ContentModelDTO.ContentDetailResponse> findAllData() {
        // Get the list
        List<Content> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid ContentModelDTO.ContentCreateRequest dto) throws BadRequestException {
        if (repository.existsByName(dto.getName())) {
            throw new BadRequestException("Content name already exist");
        }
        // set entity to add with model mapper
        Content data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, ContentModelDTO.ContentUpdateRequest dto) throws BadRequestException {
        // check exist and get
        Content data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID Content ID"));

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
            throw new BadRequestException("Content not found");
        } else {
            repository.deleteById(id);
        }
    }
}
