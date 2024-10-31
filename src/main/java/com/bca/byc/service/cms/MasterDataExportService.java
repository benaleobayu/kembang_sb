package com.bca.byc.service.cms;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface MasterDataExportService {

    void exportBusinessUser(HttpServletResponse response) throws IOException;

    void exportBlacklistKeyword(HttpServletResponse response) throws IOException;
}
