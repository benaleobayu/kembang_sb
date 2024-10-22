package com.bca.byc.service.cms.impl;

import com.bca.byc.converter.LogRequestDTOConverter;
import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.LogRequest;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.LogRequestDetailResponse;
import com.bca.byc.model.LogRequestIndexResponse;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.LogRequestRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.cms.LogRequestService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LogRequestServiceImpl implements LogRequestService {

    private LogRequestRepository repository;
    private LogRequestDTOConverter converter;

    @Override
    public List<LogRequestDetailResponse> findAllData() {
        // Get the list
        List<LogRequest> datas = repository.findAllAndOrderByName();

        // stream into the list
        return datas.stream()
                .map(converter::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ResultPageResponseDTO<LogRequestIndexResponse> listLogRequestByModelableId(String id, Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<LogRequest> pageResult = repository.listLogRequestByModelableId(id, set.keyword(), set.pageable());
        List<LogRequestIndexResponse> dtos = pageResult.stream().map((c) -> {
            LogRequestIndexResponse dto = converter.convertToIndexResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }
//
    @Override
    public LogRequestDetailResponse findDataById(String id) throws BadRequestException {
//        LogRequest data = HandlerRepository.getEntityBySecureId(id, repository, "Contact Request not found");

//        return converter.convertToListResponse(data);

        return null;
    }

    @Override
    public void deleteData(String id) throws BadRequestException {
//        LogRequest data = HandlerRepository.getEntityBySecureId(id, repository, "Contact Request not found");
//        // delete data
//        if (!repository.existsById(data.getId())) {
//            throw new BadRequestException("Contact Request not found");
//        } else {
//            repository.deleteById(data.getId());
//        }
    }
}
