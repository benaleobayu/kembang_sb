package com.bca.byc.service.cms.impl;

import com.bca.byc.service.cms.CmsUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CmsUserServiceImpl implements CmsUserService {

//    private final AppUserElasticRepository appUserElasticRepository;

//    @Override
//    public Page<AppUserElastic> getAllUsers(Integer page, Integer size) {
//        Pageable pageable = PageRequest.of(page, size);
//        var userList = appUserElasticRepository.findAllBy(pageable);
//        return new Page<>(
//                userList.stream().collect(Collectors.toList()),
//                pageable,
//                userList.getTotalElements()
//        );
//    }
}
