package com.bca.byc.service.cms;

import com.bca.byc.model.BroadcastCreateUpdateRequest;
import com.bca.byc.model.BroadcastDetailResponse;
import com.bca.byc.model.BroadcastIndexResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface BroadcastService {
    ResultPageResponseDTO<BroadcastIndexResponse> listDataBroadcast(Integer pages, Integer limit, String sortBy, String direction, String keyword, String status, LocalDate postAt);

    BroadcastDetailResponse findDataBySecureId(String id);

    void saveData(MultipartFile file, String title, String message, String status, LocalDateTime postAt) throws IOException;

    void updateData(String id, MultipartFile file, String title, String message, String status, LocalDateTime postAt) throws IOException;

    void deleteData(String id);
}
