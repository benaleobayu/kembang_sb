package com.bca.byc.service.impl;

import com.bca.byc.convert.InterestCategoryDTOConverter;
import com.bca.byc.entity.InterestCategory;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.cms.InterestCategoryModelDTO;
import com.bca.byc.repository.InterestCategoryRepository;
import com.bca.byc.service.InterestCategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InterestCategoryServiceImpl implements InterestCategoryService {

    private InterestCategoryRepository repository;
    private InterestCategoryDTOConverter converter;

    @Override
    public InterestCategoryModelDTO.DetailResponse findDataById(Long id) throws BadRequestException {
        InterestCategory data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("InterestCategory not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<InterestCategoryModelDTO.DetailResponse> findAllData() {
        // Get the list
        List<InterestCategory> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid InterestCategoryModelDTO.CreateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        InterestCategory data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, InterestCategoryModelDTO.UpdateRequest dto) throws BadRequestException {
        // check exist and get
        InterestCategory data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID InterestCategory ID"));

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
            throw new BadRequestException("InterestCategory not found");
        } else {
            repository.deleteById(id);
        }
    }
}
