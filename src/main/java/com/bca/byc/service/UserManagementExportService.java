package com.bca.byc.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface UserManagementExportService {


    void exportExcelPreRegister(HttpServletResponse response) throws IOException;

    void exportExcelUserActive(HttpServletResponse response) throws IOException;
}
