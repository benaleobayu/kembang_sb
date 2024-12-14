package com.kembang.service.impl;

import com.kembang.converter.OrderRouteDTOConverter;
import com.kembang.converter.dictionary.PageCreateReturnApps;
import com.kembang.converter.parsing.GlobalConverter;
import com.kembang.entity.AppAdmin;
import com.kembang.entity.OrderRoute;
import com.kembang.exception.BadRequestException;
import com.kembang.model.*;
import com.kembang.model.search.SavedKeywordAndPageable;
import com.kembang.repository.OrderRouteRepository;
import com.kembang.repository.auth.AppAdminRepository;
import com.kembang.repository.handler.HandlerRepository;
import com.kembang.response.ResultPageResponseDTO;
import com.kembang.security.util.ContextPrincipal;
import com.kembang.service.OrderRouteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.kembang.converter.parsing.TreeGetEntityProjection.getParsingAdminByProjection;
import static com.kembang.converter.parsing.TreeGetEntityProjection.getParsingOrderRouteByProjection;

@Service
@AllArgsConstructor
public class OrderRouteServiceImpl implements OrderRouteService {
    private final AppAdminRepository adminRepository;

    private OrderRouteRepository repository;
    private OrderRouteDTOConverter converter;

    @Override
    public ResultPageResponseDTO<OrderRouteIndexResponse> listDataOrderRoute(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        Page<OrderRoute> firstResult = repository.listDataOrderRoute(null,null);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, firstResult);

        Page<OrderRoute> pageResult = repository.listDataOrderRoute(set.keyword(), set.pageable());
        List<OrderRouteIndexResponse> dtos = pageResult.stream().map((c) -> {
            OrderRouteIndexResponse dto = converter.convertToIndexResponse(c);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, dtos);
    }

    @Override
    public OrderRouteDetailResponse findDataById(String id) throws BadRequestException {
        OrderRoute data = getParsingOrderRouteByProjection(id, repository);

        return converter.convertToDetailResponse(data);
    }

    @Override
    public void saveData(@Valid OrderRouteCreateUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        // set entity to add with model mapper
        OrderRoute data = converter.convertToCreateRequest(dto);
        data.setCreatedBy(admin.getId());
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(String id, OrderRouteCreateUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = getParsingAdminByProjection(ContextPrincipal.getSecureId(), adminRepository);
        OrderRoute data = getParsingOrderRouteByProjection(id, repository);

        // update
        converter.convertToUpdateRequest(data, dto);
        // update the updated_at
        data.setUpdatedBy(admin.getId());
        // save
        repository.save(data);
    }

    @Override
    public void deleteData(String id) throws BadRequestException {
        OrderRoute data = HandlerRepository.getEntityBySecureId(id, repository, "OrderRoute not found");
        // delete data
        if (!repository.existsById(data.getId())) {
            throw new BadRequestException("OrderRoute not found");
        } else {
            repository.deleteById(data.getId());
        }
    }
}
