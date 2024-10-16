package com.bca.byc.service.impl;

import com.bca.byc.converter.SettingsDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.Settings;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.SettingDetailResponse;
import com.bca.byc.model.SettingIndexResponse;
import com.bca.byc.model.SettingsCreateRequest;
import com.bca.byc.model.SettingsUpdateRequest;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.SettingsRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.SettingService;
import com.bca.byc.util.PaginationUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SettingsServiceImpl implements SettingService {

    private final AppAdminRepository adminRepository;

    private final SettingsRepository repository;
    private final SettingsDTOConverter converter;

    @Override
    public ResultPageResponseDTO<SettingIndexResponse> listDataSetting(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<Settings> pageResult = repository.findByNameLikeIgnoreCase(set.keyword(), set.pageable());
        List<SettingIndexResponse> dtos = pageResult.stream().map((c) -> {
            SettingIndexResponse dto = converter.convertToIndexResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public SettingDetailResponse findDataById(String id) throws BadRequestException {
        Settings data = HandlerRepository.getEntityBySecureId(id, repository, "Settings not found");

        return converter.convertToDetailResponse(data);
    }

    @Override
    public List<SettingDetailResponse> findAllData() {
        // Get the list
        List<Settings> datas = repository.findAll();

        // stream into the list
        return datas.stream()
                .map(converter::convertToDetailResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void saveData(@Valid SettingsCreateRequest dto) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        // set entity to add with model mapper
        Settings data = new Settings();
        data.setName(dto.getName());
        data.setDescription(dto.getDescription());
        data.setIsActive(dto.getStatus());
        data.setValue(data.getValue());
        // identity is generated random code 8 digit alphabets and numerics
        String convertName = dto.getName().replaceAll("\\s", "_").replaceAll("[^a-zA-Z0-9_]", "").toLowerCase();
        String identity = RandomStringUtils.randomAlphanumeric(3);
        data.setIdentity(convertName + "_" + identity);

        GlobalConverter.CmsAdminCreateAtBy(data, admin);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(String id, SettingsUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        // check exist and get
        Settings data = HandlerRepository.getEntityBySecureId(id, repository, "Settings not found");
        data.setName(dto.getName());
        data.setDescription(dto.getDescription());
        data.setValue(dto.getValue());
        data.setIsActive(dto.getStatus());

        GlobalConverter.CmsAdminUpdateAtBy(data, admin);

        // save
        repository.save(data);
    }

    @Override
    public void deleteData(String id) throws BadRequestException {
        Settings data = HandlerRepository.getEntityBySecureId(id, repository, "Settings not found");
        // delete data
        if (!repository.existsById(data.getId())) {
            throw new BadRequestException("Settings not found");
        } else {
            repository.deleteById(data.getId());
        }
    }

    @Override
    public SettingDetailResponse showByIdentity(String identity) {
        Settings data = repository.findByIdentity(identity)
                .orElseThrow(() -> new BadRequestException("identity not found"));

        return converter.convertToDetailResponse(data);
    }
}
