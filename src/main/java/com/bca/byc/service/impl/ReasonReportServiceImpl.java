package com.bca.byc.service.impl;

import com.bca.byc.converter.dictionary.PageCreateReturn;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.ReasonReport;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.ReasonReportCreateUpdateRequest;
import com.bca.byc.model.ReasonReportDetailResponse;
import com.bca.byc.model.ReasonReportIndexResponse;
import com.bca.byc.model.projection.ReasonReportProjection;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.ReasonReportRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.ReasonReportService;
import com.bca.byc.util.FileUploadHelper;
import com.bca.byc.util.helper.Formatter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReasonReportServiceImpl implements ReasonReportService {

    private final AppAdminRepository adminRepository;

    private ReasonReportRepository repository;

    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    @Value("${app.base.url}")
    private String baseUrl;

    @Override
    public List<ReasonReportIndexResponse> findAllData() {
        // Get the list
        List<ReasonReportProjection> datas = repository.findDataForPublic();

        // stream into the list
        return datas.stream()
                .map(this::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ResultPageResponseDTO<ReasonReportIndexResponse> listDataReasonReportIndex(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<ReasonReportProjection> pageResult = repository.findDataReasonReportIndex(set.keyword(), set.pageable());
        List<ReasonReportIndexResponse> dtos = pageResult.stream().map(this::convertToListResponse).collect(Collectors.toList());

        return PageCreateReturn.create(pageResult, dtos);
    }

    @Override
    public ReasonReportDetailResponse findDataById(String id) throws BadRequestException {
        ReasonReport data = reasonReport(id);
        return new ReasonReportDetailResponse(
                data.getSecureId(),
                GlobalConverter.getParseImage(data.getIcon(), baseUrl),
                data.getName(),
                data.getIsActive(),
                data.getOrders(),
                data.getIsRequired()
        );
    }

    @Override
    public void saveData(@Valid ReasonReportCreateUpdateRequest dto) throws BadRequestException, IOException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        ReasonReport data = new ReasonReport();

        saveData(data, dto);

        GlobalConverter.CmsAdminCreateAtBy(data, admin);
        repository.save(data);
    }

    @Override
    public void updateData(String id, ReasonReportCreateUpdateRequest dto) throws BadRequestException, IOException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        ReasonReport data = reasonReport(id);

        saveData(data, dto);

        GlobalConverter.CmsAdminUpdateAtBy(data, admin);
        repository.save(data);
    }

    @Override
    public void deleteData(String id) throws BadRequestException {
        ReasonReport data = reasonReport(id);
        // delete data
        if (!repository.existsById(data.getId())) {
            throw new BadRequestException("ReasonReport not found");
        } else {
            repository.deleteById(data.getId());
        }
    }

    // --- Helper ---
    private ReasonReport reasonReport(String id) {
        return HandlerRepository.getIdBySecureId(
                id,
                repository::findBySecureId,
                projection -> repository.findById(projection.getId()),
                "Reason Report not found"
        );
    }

    private void saveData(ReasonReport data, ReasonReportCreateUpdateRequest dto) throws IOException {
        data.setName(dto.name());
        data.setOrders(dto.orders());
        data.setIsActive(dto.status());
        data.setIsRequired(dto.isRequired());

        if (dto.icon() != null) {
            String oldFilePath = data.getIcon();
            FileUploadHelper.validateFileImageSvg(dto.icon());
            String filePath = FileUploadHelper.saveFile(dto.icon(), UPLOAD_DIR + "/images");
            data.setIcon(GlobalConverter.replaceImagePath(filePath));
            if (oldFilePath != null && !oldFilePath.isEmpty()) {
                FileUploadHelper.deleteFile(oldFilePath, UPLOAD_DIR);
            }
        }
    }

    private ReasonReportIndexResponse convertToListResponse(ReasonReportProjection data) {
        ReasonReportIndexResponse dto = new ReasonReportIndexResponse();
        dto.setId(data.getId());
        dto.setIndex(data.getOrders());
        dto.setIcon(GlobalConverter.getParseImage(data.getIcon(), baseUrl));
        dto.setName(data.getName());
        dto.setStatus(data.getStatus());
        dto.setIsRequired(data.getIsRequired());

        dto.setCreatedAt(Formatter.formatLocalDateTime(data.getCreatedAt()));
        dto.setUpdatedAt(Formatter.formatLocalDateTime(data.getUpdatedAt()));
        dto.setCreatedBy(data.getCreatedBy());
        dto.setUpdatedBy(data.getUpdatedBy());
        return dto;
    }

}
