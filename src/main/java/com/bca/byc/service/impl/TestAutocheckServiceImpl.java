package com.bca.byc.service.impl;

import com.bca.byc.convert.TestAutocheckDTOConverter;
import com.bca.byc.entity.TestAutocheck;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.test.TestAutocheckModelDTO;
import com.bca.byc.repository.TestAutocheckRepository;
import com.bca.byc.service.TestAutocheckService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TestAutocheckServiceImpl implements TestAutocheckService {

    private TestAutocheckRepository repository;
    private TestAutocheckDTOConverter converter;

    @Override
    public TestAutocheckModelDTO.TestAutocheckDetailResponse findDataById(Long id) throws BadRequestException {
        TestAutocheck data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("TestAutocheck not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<TestAutocheckModelDTO.TestAutocheckDetailResponse> findAllData() {
        // Get the list
        List<TestAutocheck> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid TestAutocheckModelDTO.TestAutocheckCreateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        TestAutocheck data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, TestAutocheckModelDTO.TestAutocheckUpdateRequest dto) throws BadRequestException {
        // check exist and get
        TestAutocheck data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID TestAutocheck ID"));

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
            throw new BadRequestException("TestAutocheck not found");
        } else {
            repository.deleteById(id);
        }
    }
}
