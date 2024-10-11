package com.bca.byc.service;

import com.bca.byc.model.export.ExportFilterRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface UserManagementExportService {


    void exportExcelPreRegister(HttpServletResponse response, ExportFilterRequest filter) throws IOException;

    void exportExcelUserActive(HttpServletResponse response) throws IOException;

    void exportExcelUserSuspended(HttpServletResponse response) throws IOException;

    void exportExcelUserDeleted(HttpServletResponse response) throws IOException;
}
