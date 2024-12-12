package com.kembang.service;

import com.kembang.model.CompilerFilterRequest;
import com.kembang.model.OrderCreateUpdateRequest;
import com.kembang.model.OrderDetailResponse;
import com.kembang.model.OrderIndexResponse;
import com.kembang.response.ResultPageResponseDTO;

import java.io.IOException;
import java.time.LocalDate;

public interface OrderService {
    ResultPageResponseDTO<OrderIndexResponse> listDataOrder(CompilerFilterRequest f, LocalDate date, String location, Integer route);

    OrderDetailResponse findDataBySecureId(String id);

    void saveData(OrderCreateUpdateRequest dto) throws IOException;

    void updateData(String id, OrderCreateUpdateRequest dto, Boolean isRoute) throws IOException;

    void deleteData(String id);
}
