package com.bca.byc.service;

import com.bca.byc.reponse.excel.PreRegisterExportResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public interface UserManagementExportService {


    ByteArrayInputStream exportExcelPreRegister(List<PreRegisterExportResponse> items) throws IOException;
}
