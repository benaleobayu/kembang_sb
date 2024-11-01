package com.bca.byc.service.cms.impl;

import com.bca.byc.model.export.BlacklistKeywordExportResponse;
import com.bca.byc.model.export.BranchExportResponse;
import com.bca.byc.model.export.BusinessExportResponse;
import com.bca.byc.model.export.LocationExportResponse;
import com.bca.byc.repository.BlacklistKeywordRepository;
import com.bca.byc.repository.BranchRepository;
import com.bca.byc.repository.BusinessRepository;
import com.bca.byc.repository.LocationRepository;
import com.bca.byc.service.cms.MasterDataExportService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bca.byc.converter.dictionary.ExportHelper.createRow;

@Service
@AllArgsConstructor
public class MasterDataExportServiceImpl implements MasterDataExportService {

    private final BusinessRepository businessRepository;
    private final BlacklistKeywordRepository blacklistKeywordRepository;
    private final LocationRepository locationRepository;
    private final BranchRepository branchRepository;

    @Override
    public void exportBusinessUser(HttpServletResponse response) throws IOException {
        List<BusinessExportResponse> datas = findDataForExportByUserId();

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=business_export.xls");

        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            HSSFSheet sheet = workbook.createSheet("User Business Data");

            HSSFRow headerRow = sheet.createRow(0);
            createRow(sheet, headerRow, 0, "ID");
            createRow(sheet, headerRow, 1, "Name");
            createRow(sheet, headerRow, 2, "Address");
            createRow(sheet, headerRow, 3, "Line Of Business");
            createRow(sheet, headerRow, 4, "Is Primary");
            createRow(sheet, headerRow, 5, "Locations");
            createRow(sheet, headerRow, 6, "Sub Categories");

            int dataRowIndex = 1;
            for (BusinessExportResponse data : datas) {
                HSSFRow dataRow = sheet.createRow(dataRowIndex++);
                dataRow.createCell(0).setCellValue(data.getId());
                dataRow.createCell(1).setCellValue(data.getName());
                dataRow.createCell(2).setCellValue(data.getAddress());
                dataRow.createCell(3).setCellValue(data.getLineOfBusiness());
                dataRow.createCell(4).setCellValue(data.getIsPrimary());
                dataRow.createCell(5).setCellValue(String.join(", ", data.getLocations()));
                dataRow.createCell(6).setCellValue(String.join(", ", data.getSubCategories()));
            }

            try (ServletOutputStream ops = response.getOutputStream()) {
                workbook.write(ops);
            }
        } catch (IOException e) {
            throw new IOException("Error while exporting business data", e);
        }
    }

    @Override
    public void exportBlacklistKeyword(HttpServletResponse response) throws IOException {
        List<BlacklistKeywordExportResponse> datas = blacklistKeywordRepository.findDataForExport();
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

    @Override
    public void exportLocation(HttpServletResponse response) throws IOException {
        List<LocationExportResponse> datas = locationRepository.findDataForExport();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Location");

        HSSFRow headerRow = sheet.createRow(0);
        String[] rowNames = {"ID", "Province", "Name", "Status", "Orders", "Created At", "Created By", "Updated At", "Updated By"};
        for (int i = 0; i < rowNames.length; i++) {
            createRow(sheet, headerRow, i, rowNames[i]);
        }

        int dataRowIndex = 1;
        for (LocationExportResponse data : datas) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex++);
            dataRow.createCell(0).setCellValue(data.getId());
            dataRow.createCell(1).setCellValue(data.getProvince());
            dataRow.createCell(2).setCellValue(data.getName());
            dataRow.createCell(3).setCellValue(data.getStatus());
            dataRow.createCell(4).setCellValue(data.getOrders());
            dataRow.createCell(5).setCellValue(data.getCreatedAt() != null ? data.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "");
            dataRow.createCell(6).setCellValue(data.getCreatedBy() != null ? data.getCreatedBy() : "");
            dataRow.createCell(7).setCellValue(data.getUpdatedAt() != null ? data.getUpdatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "");
            dataRow.createCell(8).setCellValue(data.getUpdatedBy() != null ? data.getUpdatedBy() : "");
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

    @Override
    public void exportBranch(HttpServletResponse response) throws IOException {
        List<BranchExportResponse> datas = branchRepository.findDataForExport();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Location");

        HSSFRow headerRow = sheet.createRow(0);
        String[] rowNames = {"ID", "Code", "Name", "Phone", "Location", "Status", "Created At", "Created By", "Updated At", "Updated By"};
        for (int i = 0; i < rowNames.length; i++) {
            createRow(sheet, headerRow, i, rowNames[i]);
        }

        int dataRowIndex = 1;
        for (BranchExportResponse data : datas) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex++);
            dataRow.createCell(0).setCellValue(data.getId());
            dataRow.createCell(1).setCellValue(data.getCode());
            dataRow.createCell(2).setCellValue(data.getName());
            dataRow.createCell(3).setCellValue(data.getPhone());
            dataRow.createCell(4).setCellValue(data.getLocationName());
            dataRow.createCell(5).setCellValue(data.getStatus());
            dataRow.createCell(6).setCellValue(data.getCreatedAt() != null ? data.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "");
            dataRow.createCell(7).setCellValue(data.getCreatedBy() != null ? data.getCreatedBy() : "");
            dataRow.createCell(8).setCellValue(data.getUpdatedAt() != null ? data.getUpdatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "");
            dataRow.createCell(9).setCellValue(data.getUpdatedBy() != null ? data.getUpdatedBy() : "");
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }


    // -------- Helper
    public List<BusinessExportResponse> findDataForExportByUserId() {
        List<Object[]> rawResults = businessRepository.findDataForExportByUserId();
        Map<Long, BusinessExportResponse> responseMap = new HashMap<>();

        for (Object[] row : rawResults) {
            Long id = (Long) row[0];
            String name = (String) row[1];
            String address = (String) row[2];
            String lineOfBusiness = (String) row[3];
            Boolean isPrimary = (Boolean) row[4];
            String location = (String) row[5];
            String subCategory = (String) row[6];

            BusinessExportResponse response = responseMap.computeIfAbsent(id, key ->
                    new BusinessExportResponse(id, name, address, lineOfBusiness, isPrimary)
            );

            if (location != null && !response.getLocations().contains(location)) {
                response.getLocations().add(location);
            }

            if (subCategory != null && !response.getSubCategories().contains(subCategory)) {
                response.getSubCategories().add(subCategory);
            }
        }

        return new ArrayList<>(responseMap.values());
    }

}
