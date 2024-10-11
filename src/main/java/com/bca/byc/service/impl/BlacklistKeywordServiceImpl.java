package com.bca.byc.service.impl;

import com.bca.byc.converter.BlacklistKeywordDTOConverter;
import com.bca.byc.converter.parsing.GlobalConverter;
import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.BlacklistKeyword;
import com.bca.byc.exception.BadRequestException;
import com.bca.byc.model.BlacklistIndexResponse;
import com.bca.byc.model.BlacklistKeywordCreateUpdateRequest;
import com.bca.byc.model.BlacklistKeywordDetailResponse;
import com.bca.byc.model.export.BlacklistKeywordExportResponse;
import com.bca.byc.model.export.UserActiveExportResponse;
import com.bca.byc.model.search.ListOfFilterPagination;
import com.bca.byc.model.search.SavedKeywordAndPageable;
import com.bca.byc.repository.BlacklistKeywordRepository;
import com.bca.byc.repository.auth.AppAdminRepository;
import com.bca.byc.repository.handler.HandlerRepository;
import com.bca.byc.response.ResultPageResponseDTO;
import com.bca.byc.service.BlacklistKeywordService;
import com.bca.byc.util.PaginationUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.bca.byc.converter.dictionary.ExportHelper.createRow;

@Service
@AllArgsConstructor
public class BlacklistKeywordServiceImpl implements BlacklistKeywordService {

    private final AppAdminRepository adminRepository;

    private BlacklistKeywordRepository repository;
    private BlacklistKeywordDTOConverter converter;

    @Override
    public ResultPageResponseDTO<BlacklistIndexResponse> listDataBlacklist(Integer pages, Integer limit, String sortBy, String direction, String keyword) {
        ListOfFilterPagination filter = new ListOfFilterPagination(
                keyword
        );
        SavedKeywordAndPageable set = GlobalConverter.createPageable(pages, limit, sortBy, direction, keyword, filter);

        Page<BlacklistKeyword> pageResult = repository.findByKeywordLikeIgnoreCase(set.keyword(), set.pageable());
        List<BlacklistIndexResponse> dtos = pageResult.stream().map((c) -> {
            BlacklistIndexResponse dto = converter.convertToListResponse(c);
            return dto;
        }).collect(Collectors.toList());

        int currentPage = pageResult.getNumber() + 1;
        int totalPages = pageResult.getTotalPages();

        return PaginationUtil.createResultPageDTO(
                pageResult.getTotalElements(), // total items
                dtos,
                currentPage, // current page
                currentPage > 1 ? currentPage - 1 : 1, // prev page
                currentPage < totalPages - 1 ? currentPage + 1 : totalPages - 1, // next page
                1, // first page
                totalPages - 1, // last page
                pageResult.getSize() // per page
        );
    }

    @Override
    public BlacklistKeywordDetailResponse findDataById(String secureId) throws BadRequestException {
        BlacklistKeyword data = HandlerRepository.getEntityBySecureId(
                secureId, repository, "BlacklistKeyword not found");

        return converter.convertToDetailResponse(data);
    }

    @Override
    public void saveData(@Valid BlacklistKeywordCreateUpdateRequest dto) throws BadRequestException {
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        // set entity to add with model mapper
        BlacklistKeyword data = converter.convertToCreateRequest(dto, admin);
        // save data
        repository.save(data);
    }

    @Override
    public void updateData(String secureId, BlacklistKeywordCreateUpdateRequest dto) throws BadRequestException {
        // check exist and get
        AppAdmin admin = GlobalConverter.getAdminEntity(adminRepository);
        BlacklistKeyword data = HandlerRepository.getEntityBySecureId(
                secureId, repository, "BlacklistKeyword not found");

        // update
        converter.convertToUpdateRequest(data, dto, admin);

        // save
        repository.save(data);
    }

    @Override
    public void deleteData(String secureId) throws BadRequestException {
        BlacklistKeyword data = HandlerRepository.getEntityBySecureId(
                secureId, repository, "BlacklistKeyword not found");
        // delete data
        if (!repository.existsById(data.getId())) {
            throw new BadRequestException("BlacklistKeyword not found");
        } else {
            repository.deleteById(data.getId());
        }
    }

    @Override
    public void exportExcel(HttpServletResponse response) throws IOException {
        List<BlacklistKeywordExportResponse> datas = repository.findDataForExport();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("User Deleted");

        HSSFRow headerRow = sheet.createRow(0);
        String[] rowNames = {"ID", "Keyword", "Status", "Orders", "Created At", "Created By", "Updated At", "Updated By"};
        for (int i = 0; i < rowNames.length; i++) {
            createRow(sheet, headerRow, i, rowNames[i]);
        }

        int dataRowIndex = 1;
        for (BlacklistKeywordExportResponse data : datas) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex++);
            dataRow.createCell(0).setCellValue(data.getId());
            dataRow.createCell(1).setCellValue(data.getKeyword());
            dataRow.createCell(2).setCellValue(data.getStatus());
            dataRow.createCell(3).setCellValue(data.getOrders());
            dataRow.createCell(4).setCellValue(data.getCreatedAt() != null ? data.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "");
            dataRow.createCell(5).setCellValue(data.getCreatedBy() != null ? data.getCreatedBy() : "");
            dataRow.createCell(6).setCellValue(data.getUpdatedAt() != null ? data.getUpdatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "");
            dataRow.createCell(7).setCellValue(data.getUpdatedBy() != null ? data.getUpdatedBy() : "");
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }
}
