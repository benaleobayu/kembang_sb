package com.bca.byc.service.impl;

import com.bca.byc.convert.ExpectItemDTOConverter;
import com.bca.byc.entity.ExpectItem;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ExpectItemModelDTO;
import com.bca.byc.repository.ExpectItemRepository;
import com.bca.byc.service.ExpectItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExpectItemServiceImpl implements ExpectItemService {

    private ExpectItemRepository repository;
    private ExpectItemDTOConverter converter;

    @Override
    public ExpectItemModelDTO.ExpectItemDetailResponse findDataById(Long id) throws BadRequestException {
        ExpectItem data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("ExpectItem not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<ExpectItemModelDTO.ExpectItemDetailResponse> findAllData() {
        // Get the list
        List<ExpectItem> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid ExpectItemModelDTO.ExpectItemCreateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        ExpectItem data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, ExpectItemModelDTO.ExpectItemUpdateRequest dto) throws BadRequestException {
        // check exist and get
        ExpectItem data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID ExpectItem ID"));

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
            throw new BadRequestException("ExpectItem not found");
        } else {
            repository.deleteById(id);
        }
    }
}
