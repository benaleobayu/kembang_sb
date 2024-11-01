package com.bca.byc.service.cms.impl;

import com.bca.byc.entity.BusinessHasCategory;
import com.bca.byc.entity.Location;
import com.bca.byc.model.export.BlacklistKeywordExportResponse;
import com.bca.byc.model.export.BranchExportResponse;
import com.bca.byc.model.export.LocationExportResponse;
import com.bca.byc.model.projection.BusinessExportProjection;
import com.bca.byc.repository.*;
import com.bca.byc.service.cms.MasterDataExportService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.bca.byc.converter.dictionary.ExportHelper.createRow;

@Service
@AllArgsConstructor
public class MasterDataExportServiceImpl implements MasterDataExportService {

    private final BusinessRepository businessRepository;
    private final BlacklistKeywordRepository blacklistKeywordRepository;
    private final LocationRepository locationRepository;
    private final BranchRepository branchRepository;

    private final BusinessHasCategoryRepository businessHasCategoryRepository;
    private final BusinessHasLocationRepository businessHasLocationRepository;


    @Override
    public void exportBusinessUser(HttpServletResponse response) throws IOException {
        List<BusinessExportProjection> datas = businessRepository.findDataForExportByUserId();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("User Business Data");

        // Create header row
        HSSFRow headerRow = sheet.createRow(0);
        String[] rowNames = {"ID", "Name", "Address", "Line Of Business", "Is Primary", "Locations", "Sub Categories", "User Email", "User Name", "User CIN"};
        for (int i = 0; i < rowNames.length; i++) {
            headerRow.createCell(i).setCellValue(rowNames[i]);
        }

        // Fetch raw data from repository and process it directly
        // Write data rows
        int dataRowIndex = 1;
        for (BusinessExportProjection data : datas) {

            List<Location> locations = businessHasLocationRepository.findLocationByBusinessId(data.getId());
            List<String> listLocation = locations.stream().map(Location::getName).toList();
            String location = String.join(", ", listLocation);

            List<BusinessHasCategory> categories = businessHasCategoryRepository.findCategoryByBusinessId(data.getId());
            List<String> listCategory = categories.stream().map(b -> b.getBusinessCategoryChild().getName()).toList();
            String category = String.join(", ", listCategory);

            HSSFRow dataRow = sheet.createRow(dataRowIndex++);
            dataRow.createCell(0).setCellValue(data.getId());
            dataRow.createCell(1).setCellValue(data.getName());
            dataRow.createCell(2).setCellValue(data.getAddress());
            dataRow.createCell(3).setCellValue(data.getLineOfBusiness());
            dataRow.createCell(4).setCellValue(data.getIsPrimary());
            dataRow.createCell(5).setCellValue(location);
            dataRow.createCell(6).setCellValue(category);
            dataRow.createCell(7).setCellValue(data.getUserEmail());
            dataRow.createCell(8).setCellValue(data.getUserName());
            dataRow.createCell(9).setCellValue(data.getUserCin());
        }

        // Write workbook to response output stream
        try (ServletOutputStream ops = response.getOutputStream()) {
            workbook.write(ops);
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


}
