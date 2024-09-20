package com.bca.byc.service.impl;

import com.bca.byc.entity.PreRegister;
import com.bca.byc.model.export.UserActiveExportResponse;
import com.bca.byc.repository.PreRegisterRepository;
import com.bca.byc.repository.UserActiveRepository;
import com.bca.byc.service.UserManagementExportService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class UserManagementExportServiceImpl implements UserManagementExportService {

    private final PreRegisterRepository preRegisterRepository;
    private final UserActiveRepository userActiveRepository;

    @Override
    public void exportExcelPreRegister(HttpServletResponse response) throws IOException {
        List<PreRegister> datas = preRegisterRepository.findAll();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Pre-Register");

        CellStyle headerStyle = createHeaderStyle(workbook);

        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("Name");
        row.getCell(0).setCellStyle(headerStyle);
        row.createCell(1).setCellValue("Email");
        row.getCell(1).setCellStyle(headerStyle);
        row.createCell(2).setCellValue("Phone");
        row.getCell(2).setCellStyle(headerStyle);
        row.createCell(3).setCellValue("Member");
        row.getCell(3).setCellStyle(headerStyle);
        row.createCell(4).setCellValue("Type");
        row.getCell(4).setCellStyle(headerStyle);
        row.createCell(5).setCellValue("Member Bank Account");
        row.getCell(5).setCellStyle(headerStyle);
        row.createCell(6).setCellValue("Member CIN");
        row.getCell(6).setCellStyle(headerStyle);
        row.createCell(7).setCellValue("Member Birthdate");
        row.getCell(7).setCellStyle(headerStyle);
        row.createCell(8).setCellValue("Child Bank Account");
        row.getCell(8).setCellStyle(headerStyle);
        row.createCell(9).setCellValue("Child CIN");
        row.getCell(9).setCellStyle(headerStyle);
        row.createCell(10).setCellValue("Child Birthdate");
        row.getCell(10).setCellStyle(headerStyle);
        row.createCell(11).setCellValue("Admin Approval Status");
        row.getCell(11).setCellStyle(headerStyle);
        row.createCell(12).setCellValue("Created At");
        row.getCell(12).setCellStyle(headerStyle);

        int dataRowIndex = 1;
        for (PreRegister data : datas) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex++);
            dataRow.createCell(0).setCellValue(data.getName());
            dataRow.createCell(1).setCellValue(data.getEmail());
            dataRow.createCell(2).setCellValue(data.getPhone());
            dataRow.createCell(3).setCellValue(data.getMemberType());
            dataRow.createCell(4).setCellValue(data.getType().toString());
            dataRow.createCell(5).setCellValue(data.getMemberBankAccount());
            dataRow.createCell(6).setCellValue(data.getMemberCin());
            dataRow.createCell(7).setCellValue(data.getMemberBirthdate().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            dataRow.createCell(8).setCellValue(data.getChildBankAccount());
            dataRow.createCell(9).setCellValue(data.getChildCin());
            dataRow.createCell(10).setCellValue(data.getChildBirthdate().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            dataRow.createCell(11).setCellValue(data.getStatusApproval() == null ? null : data.getStatusApproval().toString());
            dataRow.createCell(12).setCellValue(data.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

    @Override
    public void exportExcelUserActive(HttpServletResponse response) throws IOException {
        List<UserActiveExportResponse> datas = userActiveRepository.findDataForExport();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Pre-Register");

        CellStyle headerStyle = createHeaderStyle(workbook);

        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("ID");
        row.getCell(0).setCellStyle(headerStyle);
        row.createCell(1).setCellValue("Branch Code");
        row.getCell(1).setCellStyle(headerStyle);
        row.createCell(2).setCellValue("Name");
        row.getCell(2).setCellStyle(headerStyle);
        row.createCell(3).setCellValue("Birthdate");
        row.getCell(3).setCellStyle(headerStyle);
        row.createCell(4).setCellValue("Email");
        row.getCell(4).setCellStyle(headerStyle);
        row.createCell(5).setCellValue("CIN Number");
        row.getCell(5).setCellStyle(headerStyle);
        row.createCell(6).setCellValue("Phone Number");
        row.getCell(6).setCellStyle(headerStyle);
        row.createCell(7).setCellValue("Created At");
        row.getCell(7).setCellStyle(headerStyle);

        int dataRowIndex = 1;
        for (UserActiveExportResponse data : datas) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex++);
            dataRow.createCell(0).setCellValue(data.getId());
            dataRow.createCell(1).setCellValue(data.getBranchCode());
            dataRow.createCell(2).setCellValue(data.getName());
            dataRow.createCell(3).setCellValue(data.getBirthdate().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            dataRow.createCell(4).setCellValue(data.getEmail());
            dataRow.createCell(5).setCellValue(data.getCinNumber());
            dataRow.createCell(6).setCellValue(data.getPhoneNumber());
            dataRow.createCell(7).setCellValue(data.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        return headerStyle;
    }
}
