package com.bca.byc.service.impl;

import com.bca.byc.convert.FaqDTOConverter;
import com.bca.byc.entity.Faq;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.FaqCreateRequest;
import com.bca.byc.model.FaqDetailResponse;
import com.bca.byc.model.FaqUpdateRequest;
import com.bca.byc.repository.FaqRepository;
import com.bca.byc.service.FaqService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FaqServiceImpl implements FaqService {

    private FaqRepository repository;
    private FaqDTOConverter converter;

    @Override
    public FaqDetailResponse findDataById(Long id) throws BadRequestException {
        Faq data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Faq not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<FaqDetailResponse> findAllData() {
        // Get the list
        List<Faq> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid FaqCreateRequest dto) throws BadRequestException {
        // set entity to add with model mapper
        Faq data = converter.convertToCreateRequest(dto);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, FaqUpdateRequest dto) throws BadRequestException {
        // check exist and get
        Faq data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID Faq ID"));

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
            throw new BadRequestException("Faq not found");
        } else {
            repository.deleteById(id);
        }
    }
}
