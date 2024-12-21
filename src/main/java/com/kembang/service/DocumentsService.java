package com.kembang.service;

import com.kembang.entity.Documents;
import com.kembang.model.DocumentsDetailResponse;
import com.kembang.model.DocumentsIndexResponse;
import com.kembang.response.ResultPageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface DocumentsService {

    ResultPageResponseDTO<DocumentsIndexResponse> listIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword);

    DocumentsDetailResponse findDataBySecureId(String id);

    void saveData(MultipartFile file, String name, String identity, String folder) throws IOException;

    void updateData(String id, MultipartFile file, String name, String identity, String folder) throws IOException;

    void deleteData(String id);

    Optional<Documents> findByIdentity(String sampleBlacklist);
}
