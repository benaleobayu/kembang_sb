package com.bca.byc.service.impl;

import com.bca.byc.converter.UserActiveDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.AppUserAttribute;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.Elastic.UserActiveElastic;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.data.ListTagUserResponse;
import com.bca.byc.repository.Elastic.UserActiveElasticRepository;
import com.bca.byc.repository.UserActiveRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.UserActiveService;
import com.bca.byc.service.UserActiveUpdateRequest;
import com.bca.byc.util.PaginationUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.bca.byc.config.AppConfig.baseUrl;

@Service
@AllArgsConstructor
public class UserActiveServiceImpl implements UserActiveService {

    private UserActiveRepository repository;
    private UserActiveElasticRepository elasticRepository;
    private UserActiveDTOConverter converter;

    @Override
    public UserManagementDetailResponse findDataById(Long id) throws BadRequestException {
        AppUser data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("UserActive not found"));

        return converter.convertToListResponse(data);
    }

    @Override
    public List<UserManagementDetailResponse> findAllData() {
        // Get the list
        List<AppUser> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateData(Long id, UserActiveUpdateRequest dto) throws BadRequestException {
        // check exist and get
        AppUser data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("INVALID UserActive ID"));

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
            throw new BadRequestException("UserActive not found");
        } else {
            repository.deleteById(id);
        }
    }

    @Override
    public void suspendData(Long id) throws BadRequestException {
        AppUser data = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found"));
        AppUserAttribute attribute = data.getAppUserAttribute();
        // toggle
        attribute.setIsSuspended(!attribute.getIsSuspended().equals(true));
        // save
        data.setAppUserAttribute(attribute);
        repository.save(data);
    }

    @Override
    public ResultPageResponseDTO<UserManagementDetailResponse> listData(Integer pages,
                                                                        Integer limit,
                                                                        String sortBy,
                                                                        String direction,
                                                                        String keyword,
                                                                        Long locationId,
                                                                        LocalDate startDate,
                                                                        LocalDate endDate) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);

        // set date
        LocalDateTime start = (startDate == null) ? LocalDateTime.of(1970, 1, 1, 0, 0) : startDate.atStartOfDay();
        LocalDateTime end = (endDate == null) ? LocalDateTime.now() : endDate.atTime(23, 59, 59);

        Page<AppUser> pageResult = repository.findByKeywordAndStatusAndCreatedAt(keyword, locationId, start, end, pageable);
        List<UserManagementDetailResponse> dtos = pageResult.stream().map((c) -> {
            UserManagementDetailResponse dto = converter.convertToListResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ResultPageResponseDTO<ListTagUserResponse> listDataTagUser(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        keyword = StringUtils.isEmpty(keyword) ? "%" : keyword + "%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);

        Page<ListTagUserResponse> pageResult = repository.findListTagUser(keyword, pageable);
        List<ListTagUserResponse> dtos = pageResult.stream().map((data) -> {
            ListTagUserResponse dto = new ListTagUserResponse();

            String avatar = data.getAvatar().startsWith("uploads/") ? baseUrl + "/" + data.getAvatar() : data.getAvatar();

            dto.setAvatar(avatar);
            dto.setId(data.getId());
            dto.setName(data.getName());
            dto.setBusinessName(data.getBusinessName());
            dto.setLineOfBusiness(data.getLineOfBusiness());
            dto.setIsPrimary(data.getIsPrimary());

            return dto;
        }).collect(Collectors.toList());


        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public com.bca.byc.response.Page<UserActiveElastic> getAllActiveUser(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        var userList = elasticRepository.findAllBy(pageable);

        return new com.bca.byc.response.Page<>(
                userList.stream().collect(Collectors.toList()),
                pageable,
                userList.getTotalElements()
        );
    }

}
