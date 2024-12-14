package com.kembang.service;

import com.kembang.model.OrderRouteCreateUpdateRequest;
import com.kembang.model.OrderRouteDetailResponse;
import com.kembang.model.OrderRouteIndexResponse;
import com.kembang.response.ResultPageResponseDTO;

public interface OrderRouteService {

    ResultPageResponseDTO<OrderRouteIndexResponse> listDataOrderRoute(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    OrderRouteDetailResponse findDataById(String id);

    void saveData(OrderRouteCreateUpdateRequest item);

    void updateData(String id, OrderRouteCreateUpdateRequest item);

    void deleteData(String id);
}
