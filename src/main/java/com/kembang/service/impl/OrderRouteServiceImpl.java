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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
    public ResultPageResponseDTO<OrderRouteIndexResponse> listDataOrderRoute(CompilerFilterRequest f, LocalDate date) {
        // set default date
        LocalDate startDate = GlobalConverter.getDefaultLocalDate(date, "start");
        LocalDate endDate = GlobalConverter.getDefaultLocalDate(date, "end");

        SavedKeywordAndPageable set = GlobalConverter.createPageable(f.pages(), f.limit(), f.sortBy(), f.direction(), f.keyword());
        Page<OrderRoute> firstResult = repository.listDataOrderRoute(set.pageable(), startDate, endDate);
        Pageable pageable = GlobalConverter.oldSetPageable(f.pages(), f.limit(), f.sortBy(), f.direction(), firstResult, null);

        Page<OrderRoute> pageResult = repository.listDataOrderRoute(pageable, startDate, endDate);
        AtomicInteger index = new AtomicInteger(1);
        List<OrderRouteIndexResponse> result = pageResult.stream().map((c) ->
            converter.convertToIndexResponse(c, index)
        ).collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, result);
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
