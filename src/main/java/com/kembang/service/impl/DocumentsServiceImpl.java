package com.kembang.service.impl;

import com.kembang.converter.dictionary.PageCreateReturnApps;
import com.kembang.converter.parsing.GlobalConverter;
import com.kembang.entity.Documents;
import com.kembang.model.DocumentsDetailResponse;
import com.kembang.model.DocumentsIndexResponse;
import com.kembang.model.search.SavedKeywordAndPageable;
import com.kembang.repository.DocumentsRepository;
import com.kembang.repository.auth.AppAdminRepository;
import com.kembang.repository.handler.HandlerRepository;
import com.kembang.response.ResultPageResponseDTO;
import com.kembang.service.DocumentsService;
import com.kembang.util.FileUploadHelper;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kembang.util.FileUploadHelper.saveFile;

@Service
@AllArgsConstructor
public class DocumentsServiceImpl implements DocumentsService {

    private final AppAdminRepository adminRepository;
    private final DocumentsRepository documentsRepository;

    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    @Value("${app.base.url}")
    private String baseUrl;

    @Override
    public ResultPageResponseDTO<DocumentsIndexResponse> listIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        // on pageable
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword);
        Page<Documents> firstResult = documentsRepository.findDataByKeyword(set.keyword(), set.pageable());
        Pageable pageable = GlobalConverter.oldSetPageable(pages, limit, sortBy, direction, firstResult, null);
        // on result
        Page<Documents> pageResult = documentsRepository.findDataByKeyword(set.keyword(), pageable);
        List<DocumentsIndexResponse> dtos = pageResult.stream().map((data) -> {
            DocumentsIndexResponse dto = new DocumentsIndexResponse();
            dto.setName(data.getName());
            dto.setUrlFile(GlobalConverter.getParseImage(data.getUrlFile(), baseUrl));

            GlobalConverter.CmsIDTimeStampResponseAndId(dto, data, adminRepository);
            return dto;
        }).collect(Collectors.toList());

        return PageCreateReturnApps.create(pageResult, dtos);
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
    public void saveData(MultipartFile file, String name, String identity, String folder) throws IOException {
        if (folder.equals("documents") || folder.equals("system") || folder.equals("etc")) {
            Documents data = new Documents();
            data.setName(name);
            String convertName = identity.replaceAll("\\s", "_").replaceAll("[^a-zA-Z0-9_]", "").toLowerCase();
            data.setIdentity(convertName);
            String urlFile = FileUploadHelper.saveFile(file, UPLOAD_DIR + "/" + folder);
            data.setUrlFile(GlobalConverter.replaceImagePath(urlFile));

            documentsRepository.save(data);
        } else {
            throw new BadRequestException("Folder not found");
        }


    }

    @Override
    public void updateData(String id, MultipartFile file, String name, String identity, String folder) throws IOException {
        if (folder.equals("documents") || folder.equals("system") || folder.equals("etc")) {
            Documents data = HandlerRepository.getEntityBySecureId(id, documentsRepository, "Documents" + " not found");
            if (file != null) {
                FileUploadHelper.validateFileTypeImage(file);
                String oldFile = data.getUrlFile();
                String newFile = saveFile(file, UPLOAD_DIR + "/" + folder);
                data.setUrlFile(GlobalConverter.replaceImagePath(newFile));
                if (!oldFile.equals(newFile)) {
                    FileUploadHelper.deleteFile(oldFile, UPLOAD_DIR);
                }
            }
            data.setName(name);
            String convertName = identity.replaceAll("\\s", "_").replaceAll("[^a-zA-Z0-9_]", "").toLowerCase();
            data.setIdentity(convertName);

            documentsRepository.save(data);
        } else {
            throw new BadRequestException("Folder not found");
        }
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

    // -- ADDITIONAL --

    @Override
    public Optional<Documents> findByIdentity(String sampleBlacklist) {
        return documentsRepository.findByIdentity(sampleBlacklist);
    }
}
