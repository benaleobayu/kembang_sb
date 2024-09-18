package com.bca.byc.service;

import com.bca.byc.reponse.excel.PreRegisterExportResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public interface UserManagementExportService {


    void exportExcelPreRegister(HttpServletResponse response) throws IOException;
}
