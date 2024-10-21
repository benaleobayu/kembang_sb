package com.bca.byc.service.cms.impl;

import com.bca.byc.converter.RequestContactDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.AppUserRequestContact;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.RequestContactUpdateRequest;
import com.bca.byc.model.RequestContactDetailResponse;
import com.bca.byc.model.RequestContactIndexResponse;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.RequestContactRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.cms.RequestContactService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestContactServiceImpl implements RequestContactService {

    private final AppAdminRepository adminRepository;

    private RequestContactRepository repository;
    private RequestContactDTOConverter converter;

    @Override
    public List<RequestContactDetailResponse> findAllData() {
        // Get the list
        List<AppUserRequestContact> datas = repository.findAllAndOrderByName();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ResultPageResponseDTO<RequestContactIndexResponse> listDataRequestContact(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<AppUserRequestContact> pageResult = repository.findIdAndName(set.keyword(), set.pageable());
        List<RequestContactIndexResponse> dtos = pageResult.stream().map((c) -> {
            RequestContactIndexResponse dto = converter.convertToIndexResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public RequestContactDetailResponse findDataById(String id) throws BadRequestException {
        AppUserRequestContact data = HandlerRepository.getEntityBySecureId(id, repository, "Contact Request not found");

        return converter.convertToListResponse(data);
    }

    @Override
    public void updateData(String id, RequestContactUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        AppUserRequestContact data = HandlerRepository.getEntityBySecureId(id, repository, "Contact Request not found");

        // update
        converter.convertToUpdateRequest(data, dto);
        // update the updated_at
        data.setUpdatedBy(admin);
        // save
        repository.save(data);
    }

    @Override
    public void deleteData(String id) throws BadRequestException {
        AppUserRequestContact data = HandlerRepository.getEntityBySecureId(id, repository, "Contact Request not found");
        // delete data
        if (!repository.existsById(data.getId())) {
            throw new BadRequestException("Contact Request not found");
        } else {
            repository.deleteById(data.getId());
        }
    }
}
