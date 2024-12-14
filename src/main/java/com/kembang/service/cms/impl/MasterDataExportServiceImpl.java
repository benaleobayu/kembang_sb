package com.kembang.service.cms.impl;

import com.kembang.entity.AppAdmin;
import com.kembang.model.export.LocationExportResponse;
import com.kembang.model.export.SimpleExportResponse;
import com.kembang.repository.LocationRepository;
import com.kembang.repository.OrderRepository;
import com.kembang.repository.ProductCategoryRepository;
import com.kembang.repository.ProductRepository;
import com.kembang.repository.auth.AppAdminRepository;
import com.kembang.repository.handler.HandlerRepository;
import com.kembang.service.cms.MasterDataExportService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.kembang.converter.dictionary.ExportHelper.createRow;

@Service
@AllArgsConstructor
public class MasterDataExportServiceImpl implements MasterDataExportService {

    private final AppAdminRepository adminRepository;
    private final LocationRepository locationRepository;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final OrderRepository orderRepository;

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
            AppAdmin createdBy = HandlerRepository.getEntityById(data.getCreatedBy(), adminRepository, "Admin not found");
            AppAdmin updatedBy = HandlerRepository.getEntityById(data.getUpdatedBy(), adminRepository, "Admin not found");
            dataRow.createCell(5).setCellValue(data.getCreatedAt() != null ? data.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "");
            dataRow.createCell(6).setCellValue(data.getCreatedBy() != null ? createdBy.getName() : "");
            dataRow.createCell(7).setCellValue(data.getUpdatedAt() != null ? data.getUpdatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "");
            dataRow.createCell(8).setCellValue(data.getUpdatedBy() != null ? updatedBy.getName() : "");
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

    @Override
    public void exportProductCategory(HttpServletResponse response) throws IOException {
        List<SimpleExportResponse> datas = productCategoryRepository.findDataForExport();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Product Category");

        HSSFRow headerRow = sheet.createRow(0);
        String[] rowNames = {"ID", "Name", "Status", "Created At", "Created By", "Updated At", "Updated By"};
        for (int i = 0; i < rowNames.length; i++) {
            createRow(sheet, headerRow, i, rowNames[i]);
        }

        AtomicLong index = new AtomicLong();
        int dataRowIndex = 1;
        for (SimpleExportResponse data : datas) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex++);
            dataRow.createCell(0).setCellValue(index.getAndIncrement());
            dataRow.createCell(1).setCellValue(data.getName());
            dataRow.createCell(2).setCellValue(data.getStatus());
            dataRow.createCell(3).setCellValue(data.getCreatedAt() != null ? data.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "");
            dataRow.createCell(4).setCellValue(data.getCreatedBy() != null ? data.getCreatedBy() : "");
            dataRow.createCell(5).setCellValue(data.getUpdatedAt() != null ? data.getUpdatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "");
            dataRow.createCell(6).setCellValue(data.getUpdatedBy() != null ? data.getUpdatedBy() : "");
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

    @Override
    public void exportProduct(HttpServletResponse response) throws IOException {
        List<SimpleExportResponse> datas = productRepository.findDataForExport();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Product");

        HSSFRow headerRow = sheet.createRow(0);
        String[] rowNames = {"ID", "Name", "Status", "Created At", "Created By", "Updated At", "Updated By"};
        for (int i = 0; i < rowNames.length; i++) {
            createRow(sheet, headerRow, i, rowNames[i]);
        }

        AtomicLong index = new AtomicLong();
        int dataRowIndex = 1;
        for (SimpleExportResponse data : datas) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex++);
            dataRow.createCell(0).setCellValue(index.getAndIncrement());
            dataRow.createCell(1).setCellValue(data.getName());
            dataRow.createCell(2).setCellValue(data.getStatus());
            dataRow.createCell(3).setCellValue(data.getCreatedAt() != null ? data.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "");
            dataRow.createCell(4).setCellValue(data.getCreatedBy() != null ? data.getCreatedBy() : "");
            dataRow.createCell(5).setCellValue(data.getUpdatedAt() != null ? data.getUpdatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "");
            dataRow.createCell(6).setCellValue(data.getUpdatedBy() != null ? data.getUpdatedBy() : "");
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

    @Override
    public void exportOrder(HttpServletResponse response) throws IOException {
        List<SimpleExportResponse> datas = productRepository.findDataForExport();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Product");

        HSSFRow headerRow = sheet.createRow(0);
        String[] rowNames = {"ID", "Name", "Status", "Created At", "Created By", "Updated At", "Updated By"};
        for (int i = 0; i < rowNames.length; i++) {
            createRow(sheet, headerRow, i, rowNames[i]);
        }

        AtomicLong index = new AtomicLong();
        int dataRowIndex = 1;
        for (SimpleExportResponse data : datas) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex++);
            dataRow.createCell(0).setCellValue(index.getAndIncrement());
            dataRow.createCell(1).setCellValue(data.getName());
            dataRow.createCell(2).setCellValue(data.getStatus());
            dataRow.createCell(3).setCellValue(data.getCreatedAt() != null ? data.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "");
            dataRow.createCell(4).setCellValue(data.getCreatedBy() != null ? data.getCreatedBy() : "");
            dataRow.createCell(5).setCellValue(data.getUpdatedAt() != null ? data.getUpdatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "");
            dataRow.createCell(6).setCellValue(data.getUpdatedBy() != null ? data.getUpdatedBy() : "");
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

    @Override
    public void exportOrderRoute(HttpServletResponse response) throws IOException {
        List<SimpleExportResponse> datas = productRepository.findDataForExport();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Product");

        HSSFRow headerRow = sheet.createRow(0);
        String[] rowNames = {"ID", "Name", "Status", "Created At", "Created By", "Updated At", "Updated By"};
        for (int i = 0; i < rowNames.length; i++) {
            createRow(sheet, headerRow, i, rowNames[i]);
        }

        AtomicLong index = new AtomicLong();
        int dataRowIndex = 1;
        for (SimpleExportResponse data : datas) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex++);
            dataRow.createCell(0).setCellValue(index.getAndIncrement());
            dataRow.createCell(1).setCellValue(data.getName());
            dataRow.createCell(2).setCellValue(data.getStatus());
            dataRow.createCell(3).setCellValue(data.getCreatedAt() != null ? data.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "");
            dataRow.createCell(4).setCellValue(data.getCreatedBy() != null ? data.getCreatedBy() : "");
            dataRow.createCell(5).setCellValue(data.getUpdatedAt() != null ? data.getUpdatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "");
            dataRow.createCell(6).setCellValue(data.getUpdatedBy() != null ? data.getUpdatedBy() : "");
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }


}
