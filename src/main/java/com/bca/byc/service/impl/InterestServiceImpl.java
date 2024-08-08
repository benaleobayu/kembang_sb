package com.bca.byc.service.impl;

import com.bca.byc.convert.cms.InterestCategoryDTOConverter;
import com.bca.byc.convert.cms.InterestDTOConverter;
import com.bca.byc.entity.Business;
import com.bca.byc.entity.Interest;
import com.bca.byc.entity.InterestCategory;
import com.bca.byc.entity.User;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.api.InterestCreateRequest;
import com.bca.byc.model.api.InterestDetailResponse;
import com.bca.byc.model.api.InterestUpdateRequest;
import com.bca.byc.repository.InterestCategoryRepository;
import com.bca.byc.repository.InterestRepository;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.service.InterestService;
import com.bca.byc.service.InterestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InterestServiceImpl implements InterestService {

    private InterestRepository repository;
    private UserRepository userRepository;
    private InterestCategoryRepository categoryRepository;

    private InterestDTOConverter converter;
    private InterestCategoryDTOConverter categoryConverter;


    @Override
    public InterestDetailResponse findDataById(Long id) {

        Interest data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Interest not found"));

        return converter.convertToListResponse(data);

    }

    @Override
    public List<InterestDetailResponse> getAllData() {
        List<Interest> datas = repository.findAll();

        return datas.stream()
                .map((data -> converter.convertToListResponse(data)))
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(InterestCreateRequest dto) throws Exception {
        // check exist userId and get
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(()-> new BadRequestException("id user not found"));

        // save
        Interest interest = converter.convertToCreateRequest(dto);

        // set category
        Set<InterestCategory> categories = new HashSet<>(categoryRepository.findAllById(dto.getCategoryIds()));

        // insert all category
        interest.getCategories().addAll(categories);

        // set user_id form user
        interest.setUserId(user);

        // save all
        repository.save(interest);

    }

    @Override
    public void updateData(Long id, @Valid InterestUpdateRequest dto) {
        Interest business = repository.findById(id)
                .orElseThrow(()-> new BadRequestException("invalid.businessId"));

        // update
        converter.convertToUpdateRequest(business, dto);
        // set category
        Set<InterestCategory> categories = new HashSet<>(categoryRepository.findAllById(dto.getCategoryIds()));

        // insert all category
        business.getCategories().addAll(categories);

        // save
        repository.save(business);
    }

    @Override
    public void deleteData(Long id) {

        repository.deleteById(id);

    }

}
