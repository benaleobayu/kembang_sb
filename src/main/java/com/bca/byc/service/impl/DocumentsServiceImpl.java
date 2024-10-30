package com.bca.byc.service.impl;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.Documents;
import com.bca.byc.model.DocumentsDetailResponse;
import com.bca.byc.model.DocumentsIndexResponse;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.DocumentsRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.DocumentsService;
import com.bca.byc.util.FileUploadHelper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.bca.byc.util.FileUploadHelper.saveFile;

@Service
@AllArgsConstructor
public class DocumentsServiceImpl implements DocumentsService {

    private final DocumentsRepository documentsRepository;

    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    @Value("${app.base.url}")
    private String baseUrl;

    @Override
    public ResultPageResponseDTO<DocumentsIndexResponse> listIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(keyword);
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<Documents> pageResult = documentsRepository.findDataByKeyword(set.keyword(), set.pageable());
        List<DocumentsIndexResponse> dtos = pageResult.stream().map((data) -> {
            DocumentsIndexResponse dto = new DocumentsIndexResponse();
            dto.setName(data.getName());
            dto.setUrlFile(GlobalConverter.getParseImage(data.getUrlFile(), baseUrl));

            GlobalConverter.CmsIDTimeStampResponseAndId(dto, data);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public DocumentsDetailResponse findDataBySecureId(String id) {
        Documents data = HandlerRepository.getEntityBySecureId(id, documentsRepository, "Documents" + " not found");

        DocumentsDetailResponse dto = new DocumentsDetailResponse();
        dto.setId(data.getSecureId());
        dto.setName(data.getName());
        dto.setUrlFile(GlobalConverter.getParseImage(data.getUrlFile(), baseUrl));
        return dto;
    }

    @Override
    public void saveData(MultipartFile file, String name) throws IOException {
        Documents data = new Documents();
        data.setName(name);
        String urlFile = FileUploadHelper.saveFile(file, UPLOAD_DIR + "/documents");
        data.setUrlFile(GlobalConverter.replaceImagePath(urlFile));

        documentsRepository.save(data);

    }

    @Override
    public void updateData(String id, MultipartFile file, String name) throws IOException {
        Documents data = HandlerRepository.getEntityBySecureId(id, documentsRepository, "Documents" + " not found");
        if (file != null) {
            FileUploadHelper.validateFileTypeImage(file);
            String oldFile = data.getUrlFile();
            String newFile = saveFile(file, UPLOAD_DIR + "/documents");
            data.setUrlFile(GlobalConverter.replaceImagePath(newFile));
            if (!oldFile.equals(newFile)) {
                FileUploadHelper.deleteFile(oldFile, UPLOAD_DIR);
            }
        }
        data.setName(name);

        documentsRepository.save(data);
    }

    @Override
    public void deleteData(String id) {
        Documents data = HandlerRepository.getEntityBySecureId(id, documentsRepository, "Documents" + " not found");
        if (!documentsRepository.existsById(data.getId())) {
            throw new RuntimeException("Documents not found");
        } else {
            documentsRepository.deleteById(data.getId());
        }
    }
}
