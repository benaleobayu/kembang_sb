package com.bca.byc.service.impl;

import com.bca.byc.model.dto.AppUserDetailNameQueryDTO;
import com.bca.byc.model.dto.AppUserDetailPhoneQueryDTO;
import com.bca.byc.repository.UserProjectionRepository;
import com.bca.byc.service.UserProjectionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserProjectionServiceImpl implements UserProjectionService {

    private final UserProjectionRepository userProjectionRepository;

    @Override
    public Map<Long, String> findUserNameMaps(List<Long> idList) {
        List<AppUserDetailNameQueryDTO> queryList = userProjectionRepository.findUserNameByIdList(idList);
        Map<Long, String> authorMap = new HashMap<>();
        for (AppUserDetailNameQueryDTO q : queryList) {
            authorMap.put(q.getId(), q.getName());
        }
        return authorMap;
    }
    @Override
    public Map<Long, String> findUserPhoneMaps(List<Long> idList) {
        List<AppUserDetailPhoneQueryDTO> queryList = userProjectionRepository.findUserPhoneByIdList(idList);
        Map<Long, String> authorMap = new HashMap<>();
        for (AppUserDetailPhoneQueryDTO q : queryList) {
            if (!authorMap.containsKey(q.getId())) {
                authorMap.put(q.getId(), q.getPhoneNumber());
            }
        }
        return authorMap;
    }
}
