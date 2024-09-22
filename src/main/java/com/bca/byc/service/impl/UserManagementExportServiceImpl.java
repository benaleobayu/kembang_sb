package com.bca.byc.service.impl;

import com.bca.byc.entity.PreRegister;
import com.bca.byc.model.export.UserActiveExportResponse;
import com.bca.byc.repository.PreRegisterRepository;
import com.bca.byc.repository.UserActiveRepository;
import com.bca.byc.repository.UserDeletedRepository;
import com.bca.byc.repository.UserSuspendedRepository;
import com.bca.byc.service.UserManagementExportService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFCell;
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
    private final UserSuspendedRepository userSuspendedRepository;
    private final UserDeletedRepository userDeletedRepository;

    @Override
    public void exportExcelPreRegister(HttpServletResponse response) throws IOException {
        List<PreRegister> datas = preRegisterRepository.findAll();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Pre-Register");

        HSSFRow headerRow = sheet.createRow(0);
        createRow(sheet, headerRow, 0, "Name");
        createRow(sheet, headerRow, 1, "Email");
        createRow(sheet, headerRow, 2, "Phone");
        createRow(sheet, headerRow, 3, "Member");
        createRow(sheet, headerRow, 4, "Type");
        createRow(sheet, headerRow, 5, "Member Bank Account");
        createRow(sheet, headerRow, 6, "Member CIN");
        createRow(sheet, headerRow, 7, "Member Birthdate");
        createRow(sheet, headerRow, 8, "Child Bank Account");
        createRow(sheet, headerRow, 9, "Child CIN");
        createRow(sheet, headerRow, 10, "Child Birthdate");
        createRow(sheet, headerRow, 11, "Admin Approval Status");
        createRow(sheet, headerRow, 12, "Created At");

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
            dataRow.createCell(8).setCellValue(data.getParentBankAccount());
            dataRow.createCell(9).setCellValue(data.getParentCin());
            dataRow.createCell(10).setCellValue(data.getParentBirthdate().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")));
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
        HSSFSheet sheet = workbook.createSheet("User Active");

        HSSFRow headerRow = sheet.createRow(0);
        createRow(sheet, headerRow, 0, "ID");
        createRow(sheet, headerRow, 1, "Branch Code");
        createRow(sheet, headerRow, 2, "Name");
        createRow(sheet, headerRow, 3, "Birthday");
        createRow(sheet, headerRow, 4, "Email");
        createRow(sheet, headerRow, 5, "Cin Number");
        createRow(sheet, headerRow, 6, "Phone Number");
        createRow(sheet, headerRow, 7, "Created At");

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

    @Override
    public void exportExcelUserSuspended(HttpServletResponse response) throws IOException {
        List<UserActiveExportResponse> datas = userSuspendedRepository.findDataForExport();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("User Suspended");

        HSSFRow headerRow = sheet.createRow(0);
        createRow(sheet, headerRow, 0, "ID");
        createRow(sheet, headerRow, 1, "Branch Code");
        createRow(sheet, headerRow, 2, "Name");
        createRow(sheet, headerRow, 3, "Birthday");
        createRow(sheet, headerRow, 4, "Email");
        createRow(sheet, headerRow, 5, "Cin Number");
        createRow(sheet, headerRow, 6, "Phone Number");
        createRow(sheet, headerRow, 7, "Created At");

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

    @Override
    public void exportExcelUserDeleted(HttpServletResponse response) throws IOException {
        List<UserActiveExportResponse> datas = userDeletedRepository.findDataForExport();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("User Deleted");

        HSSFRow headerRow = sheet.createRow(0);
        createRow(sheet, headerRow, 0, "ID");
        createRow(sheet, headerRow, 1, "Branch Code");
        createRow(sheet, headerRow, 2, "Name");
        createRow(sheet, headerRow, 3, "Birthday");
        createRow(sheet, headerRow, 4, "Email");
        createRow(sheet, headerRow, 5, "Cin Number");
        createRow(sheet, headerRow, 6, "Phone Number");
        createRow(sheet, headerRow, 7, "Created At");

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

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        return headerStyle;
    }

    private static void createRow(HSSFSheet sheet, HSSFRow row, int colIndex, String rowName) {
        HSSFCell cell = row.createCell(colIndex);
        cell.setCellValue(rowName);
        cell.setCellStyle(createHeaderStyle(sheet.getWorkbook()));
    }
}
