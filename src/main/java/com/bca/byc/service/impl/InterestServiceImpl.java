package com.bca.byc.service.impl;

import com.bca.byc.convert.InterestDTOConverter;
import com.bca.byc.entity.Interest;
import com.bca.byc.entity.InterestCategory;
import com.bca.byc.entity.User;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.InterestModelDTO;
import com.bca.byc.repository.InterestCategoryRepository;
import com.bca.byc.repository.InterestRepository;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.service.InterestService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InterestServiceImpl implements InterestService {

    private InterestRepository repository;
    private InterestCategoryRepository categoryRepository;
    private UserRepository userRepository;
    private InterestDTOConverter converter;

    @Override
    public InterestModelDTO.InterestDetailResponse findDataById(Long id) throws BadRequestException {
        Interest data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("Interest not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<InterestModelDTO.InterestDetailResponse> findAllData() {
        // Get the list
        List<Interest> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid InterestModelDTO.InterestCreateRequest dto) throws BadRequestException {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(()-> new BadRequestException("id user not found"));

        // set entity to add with model mapper
        Interest data = converter.convertToCreateRequest(dto);
        // set category
        Set<InterestCategory> categories = new HashSet<>(categoryRepository.findAllById(dto.getCategoryIds()));

// insert all category
        data.getCategories().addAll(categories);

// set user_id form user
        data.setUserId(user);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(Long id, InterestModelDTO.InterestUpdateRequest dto) throws BadRequestException {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(()-> new BadRequestException("id user not found"));

        // check exist and get
        Interest data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID Interest ID"));

        // update
        converter.convertToUpdateRequest(data, dto);

        // set category
        Set<InterestCategory> categories = new HashSet<>(categoryRepository.findAllById(dto.getCategoryIds()));

        // update the updated_at
        data.setUpdatedAt(LocalDateTime.now());

        // insert all category
        data.getCategories().addAll(categories);

        // set user_id form user
        data.setUserId(user);

        // save
        repository.save(data);
    }

    @Override
    public void deleteData(Long id) throws BadRequestException {
        // delete data
        if (!repository.existsById(id)) {
            throw new BadRequestException("Interest not found");
        } else {
            repository.deleteById(id);
        }
    }
}
