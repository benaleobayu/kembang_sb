package com.bca.byc.service.impl;

import com.bca.byc.convert.cms.BusinessDTOConverter;
import com.bca.byc.entity.Business;
import com.bca.byc.entity.User;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.api.BusinessCreateRequest;
import com.bca.byc.model.api.BusinessDetailResponse;
import com.bca.byc.model.api.BusinessUpdateRequest;
import com.bca.byc.repository.BusinessRepository;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.service.BusinessService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private BusinessRepository repository;
    private UserRepository userRepository;

    private BusinessDTOConverter converter;


    @Override
    public BusinessDetailResponse findDataById(Long id) {

        Business data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Business not found"));
        return converter.convertToListResponse(data);
    }

    @Override
    public List<BusinessDetailResponse> getAllData() {
        List<Business> datas = repository.findAll();

        return datas.stream()
                .map((data -> converter.convertToListResponse(data)))
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(BusinessCreateRequest dto) throws Exception {
        // check exist userId
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(()-> new BadRequestException("id user not found"));

        // save
        Business business = converter.convertToCreateRequest(dto);

        business.setUserId(user);

        repository.save(business);

    }

    @Override
    public void updateData(Long id, @Valid BusinessUpdateRequest dto) {
        Business business = repository.findById(id)
                .orElseThrow(()-> new BadRequestException("invalid.businessId"));

        // update
        converter.convertToUpdateRequest(dto);

        // set updatedAt
        business.setUpdatedAt(LocalDateTime.now());

        // save
        repository.save(business);
    }

    @Override
    public void deleteData(Long id) {

        repository.deleteById(id);

    }

}
