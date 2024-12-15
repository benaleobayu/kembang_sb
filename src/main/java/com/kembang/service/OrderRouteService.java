package com.kembang.service;

import com.kembang.model.CompilerFilterRequest;
import com.kembang.model.OrderRouteCreateUpdateRequest;
import com.kembang.model.OrderRouteDetailResponse;
import com.kembang.model.OrderRouteIndexResponse;
import com.kembang.response.ResultPageResponseDTO;

import java.time.LocalDate;

public interface OrderRouteService {

    ResultPageResponseDTO<OrderRouteIndexResponse> listDataOrderRoute(CompilerFilterRequest f, LocalDate parseDate);

    OrderRouteDetailResponse findDataById(String id);

    void saveData(OrderRouteCreateUpdateRequest item);

    void updateData(String id, OrderRouteCreateUpdateRequest item);

    void deleteData(String id);
}
