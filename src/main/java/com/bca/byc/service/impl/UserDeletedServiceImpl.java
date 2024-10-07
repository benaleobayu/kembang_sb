package com.bca.byc.service.impl;

import com.bca.byc.converter.UserDeletedDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.entity.AppUser;
import com.bca.byc.entity.AppUserAttribute;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.UserManagementDetailResponse;
import com.bca.byc.model.UserManagementListResponse;
import com.bca.byc.model.projection.CMSBulkDeleteProjection;
import com.bca.byc.repository.UserDeletedRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.UserDeletedService;
import com.bca.byc.util.PaginationUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bca.byc.converter.parsing.TreeUserManagementConverter.IndexResponse;

@Service
@RequiredArgsConstructor
public class UserDeletedServiceImpl implements UserDeletedService {

    private final UserDeletedRepository repository;

    private final UserDeletedDTOConverter converter;

    @Override
    public ResultPageResponseDTO<UserManagementListResponse> listData(Integer pages,
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

        Page<AppUser> pageResult = repository.findByKeywordAndStatusAndDeletedAndCreatedAt(keyword, locationId, start, end, pageable);
        List<UserManagementListResponse> dtos = pageResult.stream().map((c) -> {
            UserManagementListResponse dto = new UserManagementListResponse();
            IndexResponse(c,dto);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public UserManagementDetailResponse findDataById(String id) throws BadRequestException {
        AppUser data = HandlerRepository.getEntityBySecureId(id, repository, "User not found");

        return converter.convertToListResponse(data);
    }

    @Override
    public void makeUserIsDeletedFalse(String id) {
        AppUser user = HandlerRepository.getEntityBySecureId(id, repository, "User not found");
        AppUserAttribute userAttribute = user.getAppUserAttribute();
        userAttribute.setIsDeleted(false);
        user.setAppUserAttribute(userAttribute);
        repository.save(user);
    }

    @Override
    public void makeUserBulkRestoreTrue(Set<String> ids) {
        Set<CMSBulkDeleteProjection> userProjections = repository.findBySecureIdIn(ids);

        userProjections.forEach(projection -> {
            AppUser user = repository.findById(projection.getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            AppUserAttribute userAttribute = user.getAppUserAttribute();
            userAttribute.setIsDeleted(false);
            user.setAppUserAttribute(userAttribute);
            repository.save(user);
        });
    }

}
