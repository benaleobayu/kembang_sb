package com.bca.byc.service;

import com.bca.byc.model.DocumentsDetailResponse;
import com.bca.byc.model.DocumentsIndexResponse;
import com.bca.byc.response.ResultPageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DocumentsService {

    ResultPageResponseDTO<DocumentsIndexResponse> listIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    DocumentsDetailResponse findDataBySecureId(String id);

    void saveData(MultipartFile file, String name) throws IOException;

    void updateData(String id, MultipartFile file, String name) throws IOException;

    void deleteData(String id);
}
