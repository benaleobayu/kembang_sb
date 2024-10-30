package com.bca.byc.service.cms;

import com.bca.byc.model.BroadcastCreateUpdateRequest;
import com.bca.byc.model.BroadcastDetailResponse;
import com.bca.byc.model.BroadcastIndexResponse;
import com.bca.byc.response.ResultPageResponseDTO;

import java.time.LocalDate;

public interface BroadcastService {
    ResultPageResponseDTO<BroadcastIndexResponse> listDataBroadcast(Integer pages, Integer limit, String sortBy, String direction, String keyword, String status, LocalDate postAt);

    BroadcastDetailResponse findDataBySecureId(String id);

    void saveData(BroadcastCreateUpdateRequest item);

    void updateData(String id, BroadcastCreateUpdateRequest item);

    void deleteData(String id);
}
