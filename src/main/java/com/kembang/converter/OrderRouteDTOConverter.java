package com.kembang.converter;

import com.kembang.converter.parsing.GlobalConverter;
import com.kembang.entity.OrderRoute;
import com.kembang.model.OrderRouteCreateUpdateRequest;
import com.kembang.model.OrderRouteDetailResponse;
import com.kembang.model.OrderRouteIndexResponse;
import com.kembang.repository.auth.AppAdminRepository;
import com.kembang.util.helper.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@AllArgsConstructor
public class OrderRouteDTOConverter {

    private final AppAdminRepository adminRepository;
    private ModelMapper modelMapper;

    //for get index data
    public OrderRouteIndexResponse convertToIndexResponse(OrderRoute data, AtomicInteger index) {
        // mapping Entity with DTO Entity
        OrderRouteIndexResponse dto = modelMapper.map(data, OrderRouteIndexResponse.class);

        // get day name
        String dayName = Formatter.formatGetDayName(data.getDate());

        GlobalConverter.CmsIDTimeStampResponseAndIdAtomic(dto, data, adminRepository, index);
        dto.setDate(dayName + ", " + Formatter.formatLocalDate(data.getDate()));

        dto.setTotalDistance(data.getTotalDistance() != null ? data.getTotalDistance() : BigDecimal.valueOf(0));
        dto.setTotalCost(data.getTotalCost() != null ? data.getTotalCost() : 0);
        dto.setTotalRemainingCost(data.getTotalRemainingCost() != null ? data.getTotalRemainingCost() : 0);
        // return
        return dto;
    }

    // for get detail data
    public OrderRouteDetailResponse convertToDetailResponse(OrderRoute data) {
        // mapping Entity with DTO Entity
        return new OrderRouteDetailResponse(
                data.getSecureId(),
                data.getRoute(),
                data.getDriverName(),
                Formatter.formatLocalDate(data.getDate()),
                data.getTotalDistance(),
                data.getTotalCost(),
                data.getTotalRemainingCost()
        );
    }

    // for create data
    public OrderRoute convertToCreateRequest(@Valid OrderRouteCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        OrderRoute data = modelMapper.map(dto, OrderRoute.class);
        // return
        return data;
    }

    // for update data
    public void convertToUpdateRequest(OrderRoute data, @Valid OrderRouteCreateUpdateRequest dto) {
        // mapping DTO Entity with Entity
        modelMapper.map(dto, data);
        // set updated_at
        data.setUpdatedAt(LocalDateTime.now());
    }
}
