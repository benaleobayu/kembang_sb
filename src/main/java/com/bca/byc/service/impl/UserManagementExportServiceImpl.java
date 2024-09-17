package com.bca.byc.service.impl;

import com.bca.byc.reponse.excel.PreRegisterExportResponse;
import com.bca.byc.service.UserManagementExportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class UserManagementExportServiceImpl implements UserManagementExportService {
    @Override
    public ByteArrayInputStream exportExcelPreRegister(List<PreRegisterExportResponse> items) throws IOException {
        String[] columns = {"Name", "Email", "Created At", "Updated At"};
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Users");

            // Header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(createHeaderStyle(workbook));
            }

            // Body
            int rowIdx = 1;
            int index = 0;
            for (PreRegisterExportResponse data : items) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(index).setCellValue(data.getId());
                row.createCell(index+1).setCellValue(data.getName());
                row.createCell(index + 1).setCellValue(data.getEmail());
                row.createCell(index + 1).setCellValue(data.getMemberBirthdate());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        return headerStyle;
    }
}
