package com.kembang.service.cms;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface MasterDataExportService {

    void exportLocation(HttpServletResponse response) throws IOException;

    void exportProductCategory(HttpServletResponse response) throws IOException;

    void exportProduct(HttpServletResponse response) throws IOException;

    void exportOrder(HttpServletResponse response) throws IOException;
}
