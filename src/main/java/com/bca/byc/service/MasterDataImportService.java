package com.bca.byc.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MasterDataImportService {

    void importBlacklistKeyword(MultipartFile file) throws IOException;
}
