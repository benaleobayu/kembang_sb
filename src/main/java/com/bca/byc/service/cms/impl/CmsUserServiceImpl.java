package com.bca.byc.service.cms.impl;

import com.bca.byc.entity.Elastic.AppUserElastic;
import com.bca.byc.repository.AppUserElasticRepository;
import com.bca.byc.repository.auth.AppUserRepository;
import com.bca.byc.response.Page;
import com.bca.byc.service.cms.CmsUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CmsUserServiceImpl implements CmsUserService {

    private final AppUserRepository appUserRepository;
    private final AppUserElasticRepository appUserElasticRepository;

    @Override
    public Page<AppUserElastic> getAllUsers(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        var userList = appUserElasticRepository.findAllBy(pageable);
        return new Page<>(
                userList.stream().collect(Collectors.toList()),
                pageable,
                userList.getTotalElements()
        );
    }

    @Override
    public Long countAllUsers() {
        return appUserRepository.count();
    }
}
