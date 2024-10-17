package com.bca.byc.service.impl;

import com.bca.byc.model.export.ExportFilterRequest;
import com.bca.byc.model.export.PreRegisterExportResponse;
import com.bca.byc.model.export.UserActiveExportResponse;
import com.bca.byc.repository.PreRegisterRepository;
import com.bca.byc.repository.UserActiveRepository;
import com.bca.byc.repository.UserDeletedRepository;
import com.bca.byc.repository.UserSuspendedRepository;
import com.bca.byc.service.UserManagementExportService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.bca.byc.converter.dictionary.ExportHelper.createRow;

@Service
@AllArgsConstructor
public class UserManagementExportServiceImpl implements UserManagementExportService {

    private final PreRegisterRepository preRegisterRepository;
    private final UserActiveRepository userActiveRepository;
    private final UserSuspendedRepository userSuspendedRepository;
    private final UserDeletedRepository userDeletedRepository;

    @Override
    public void exportExcelPreRegister(HttpServletResponse response, ExportFilterRequest filter) throws IOException {
        // set date
        LocalDateTime start = (filter.getStartDate() == null) ? LocalDateTime.of(1970, 1, 1, 0, 0) : filter.getStartDate().atStartOfDay();
        LocalDateTime end = (filter.getEndDate() == null) ? LocalDateTime.now() : filter.getEndDate().atTime(23, 59, 59);

        List<PreRegisterExportResponse> datas = preRegisterRepository.findDataForExport(start, end, filter.getStatus());
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Pre-Register");

        HSSFRow headerRow = sheet.createRow(0);
        createRow(sheet, headerRow, 0, "ID");
        createRow(sheet, headerRow, 1, "Name");
        createRow(sheet, headerRow, 2, "Email");
        createRow(sheet, headerRow, 3, "Phone");
        createRow(sheet, headerRow, 4, "Member");
        createRow(sheet, headerRow, 5, "Type");
        createRow(sheet, headerRow, 6, "Member Bank Account");
        createRow(sheet, headerRow, 7, "Member CIN");
        createRow(sheet, headerRow, 8, "Member Birthdate");
        createRow(sheet, headerRow, 9, "Child Bank Account");
        createRow(sheet, headerRow, 10, "Child CIN");
        createRow(sheet, headerRow, 11, "Child Birthdate");
        createRow(sheet, headerRow, 12, "Branch Code");
        createRow(sheet, headerRow, 13, "Pic Name");
        createRow(sheet, headerRow, 14, "Admin Approval Status");
        createRow(sheet, headerRow, 15, "Created At");
        createRow(sheet, headerRow, 16, "Created By");

        int dataRowIndex = 1;
        for (PreRegisterExportResponse data : datas) {
            String status = switch (data.getAdminApprovalStatus().ordinal()) {
                case 0 -> "Draft";
                case 1 -> "Waiting Approval";
                case 4 -> "Reject";
                case 5 -> "Approve";
                default ->
                        throw new IllegalStateException("Unexpected value: " + data.getAdminApprovalStatus().ordinal());
            };
            HSSFRow dataRow = sheet.createRow(dataRowIndex++);
            dataRow.createCell(0).setCellValue(dataRowIndex -1);
            dataRow.createCell(1).setCellValue(data.getName() == null ? null : data.getName());
            dataRow.createCell(2).setCellValue(data.getEmail() == null ? null : data.getEmail());
            dataRow.createCell(3).setCellValue(data.getPhone() == null ? null : data.getPhone());
            dataRow.createCell(4).setCellValue(data.getType() == null ? null : data.getType());
            dataRow.createCell(5).setCellValue(data.getMemberType() == null ? null : data.getMemberType().name());
            dataRow.createCell(6).setCellValue(data.getMemberBankAccount() == null ? null : data.getMemberBankAccount());
            dataRow.createCell(7).setCellValue(data.getMemberCin() == null ? null : data.getMemberCin());
            dataRow.createCell(8).setCellValue(data.getMemberBirthdate() == null ? null : data.getMemberBirthdate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            dataRow.createCell(9).setCellValue(data.getParentBankAccount() == null ? null : data.getParentBankAccount());
            dataRow.createCell(10).setCellValue(data.getParentCin() == null ? null : data.getParentCin());
            dataRow.createCell(11).setCellValue(data.getParentBirthdate() == null ? null : data.getParentBirthdate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            dataRow.createCell(12).setCellValue(data.getBranch() == null ? null : data.getBranch());
            dataRow.createCell(13).setCellValue(data.getPicName() == null ? null : data.getPicName());
            dataRow.createCell(14).setCellValue(data.getAdminApprovalStatus() == null ? null : status);
            dataRow.createCell(15).setCellValue(data.getCreatedAt() == null ? null : data.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            dataRow.createCell(16).setCellValue(data.getCreatedBy() == null ? null : data.getCreatedBy());
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

    @Override
    public void exportExcelUserActive(HttpServletResponse response, ExportFilterRequest filter) throws IOException {
        // set date
        LocalDateTime start = (filter.getStartDate() == null) ? LocalDateTime.of(1970, 1, 1, 0, 0) : filter.getStartDate().atStartOfDay();
        LocalDateTime end = (filter.getEndDate() == null) ? LocalDateTime.now() : filter.getEndDate().atTime(23, 59, 59);

        List<UserActiveExportResponse> datas = userActiveRepository.findDataForExport(start, end, filter.getSegmentation(), filter.getLocationId(), filter.getIsSenior());
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("User Active");

        HSSFRow headerRow = sheet.createRow(0);
        String[] rowNames = {"ID", "Branch", "Name", "Birthday", "Email", "Cin Number", "Segmentationt", "Phone Number", "Created At"};
        for (int i = 0; i < rowNames.length; i++) {
            createRow(sheet, headerRow, i, rowNames[i]);
        }

        int dataRowIndex = 1;
        for (UserActiveExportResponse data : datas) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex++);
            dataRow.createCell(0).setCellValue(dataRowIndex -1);
            dataRow.createCell(1).setCellValue(data.getBranch() == null ? null : data.getBranch());
            dataRow.createCell(2).setCellValue(data.getName() == null ? null : data.getName());
            dataRow.createCell(3).setCellValue(data.getBirthdate() == null ? null : data.getBirthdate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            dataRow.createCell(4).setCellValue(data.getEmail() == null ? null : data.getEmail());
            dataRow.createCell(5).setCellValue(data.getCinNumber() == null ? null : data.getCinNumber());
            dataRow.createCell(6).setCellValue(data.getSegmentation() == null ? null : data.getSegmentation().name());
            dataRow.createCell(7).setCellValue(data.getPhoneNumber() == null ? null : data.getPhoneNumber());
            dataRow.createCell(8).setCellValue(data.getCreatedAt() == null ? null : data.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

    @Override
    public void exportExcelUserSuspended(HttpServletResponse response, ExportFilterRequest filter) throws IOException {
        // set date
        LocalDateTime start = (filter.getStartDate() == null) ? LocalDateTime.of(1970, 1, 1, 0, 0) : filter.getStartDate().atStartOfDay();
        LocalDateTime end = (filter.getEndDate() == null) ? LocalDateTime.now() : filter.getEndDate().atTime(23, 59, 59);

        List<UserActiveExportResponse> datas = userSuspendedRepository.findDataForExport(start, end, filter.getSegmentation(), filter.getLocationId(), filter.getIsSenior());
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("User Active");

        HSSFRow headerRow = sheet.createRow(0);
        String[] rowNames = {"ID", "Branch", "Name", "Birthday", "Email", "Cin Number", "Segmentationt", "Phone Number", "Created At"};
        for (int i = 0; i < rowNames.length; i++) {
            createRow(sheet, headerRow, i, rowNames[i]);
        }

        int dataRowIndex = 1;
        for (UserActiveExportResponse data : datas) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex++);
            dataRow.createCell(0).setCellValue(dataRowIndex -1);
            dataRow.createCell(1).setCellValue(data.getBranch() == null ? null : data.getBranch());
            dataRow.createCell(2).setCellValue(data.getName() == null ? null : data.getName());
            dataRow.createCell(3).setCellValue(data.getBirthdate() == null ? null : data.getBirthdate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            dataRow.createCell(4).setCellValue(data.getEmail() == null ? null : data.getEmail());
            dataRow.createCell(5).setCellValue(data.getCinNumber() == null ? null : data.getCinNumber());
            dataRow.createCell(6).setCellValue(data.getSegmentation() == null ? null : data.getSegmentation().name());
            dataRow.createCell(7).setCellValue(data.getPhoneNumber() == null ? null : data.getPhoneNumber());
            dataRow.createCell(8).setCellValue(data.getCreatedAt() == null ? null : data.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

    @Override
    public void exportExcelUserDeleted(HttpServletResponse response, ExportFilterRequest filter) throws IOException {
        // set date
        LocalDateTime start = (filter.getStartDate() == null) ? LocalDateTime.of(1970, 1, 1, 0, 0) : filter.getStartDate().atStartOfDay();
        LocalDateTime end = (filter.getEndDate() == null) ? LocalDateTime.now() : filter.getEndDate().atTime(23, 59, 59);

        List<UserActiveExportResponse> datas = userDeletedRepository.findDataForExport(start, end, filter.getSegmentation(), filter.getLocationId(), filter.getIsSenior());
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("User Active");

        HSSFRow headerRow = sheet.createRow(0);
        String[] rowNames = {"ID", "Branch", "Name", "Birthday", "Email", "Cin Number", "Segmentationt", "Phone Number", "Created At"};
        for (int i = 0; i < rowNames.length; i++) {
            createRow(sheet, headerRow, i, rowNames[i]);
        }

        int dataRowIndex = 1;
        for (UserActiveExportResponse data : datas) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex++);
            dataRow.createCell(0).setCellValue(dataRowIndex -1);
            dataRow.createCell(1).setCellValue(data.getBranch() == null ? null : data.getBranch());
            dataRow.createCell(2).setCellValue(data.getName() == null ? null : data.getName());
            dataRow.createCell(3).setCellValue(data.getBirthdate() == null ? null : data.getBirthdate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            dataRow.createCell(4).setCellValue(data.getEmail() == null ? null : data.getEmail());
            dataRow.createCell(5).setCellValue(data.getCinNumber() == null ? null : data.getCinNumber());
            dataRow.createCell(6).setCellValue(data.getSegmentation() == null ? null : data.getSegmentation().name());
            dataRow.createCell(7).setCellValue(data.getPhoneNumber() == null ? null : data.getPhoneNumber());
            dataRow.createCell(8).setCellValue(data.getCreatedAt() == null ? null : data.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

}
