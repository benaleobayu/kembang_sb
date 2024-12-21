package com.kembang.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImportService {
    void CustomerImport(MultipartFile file) throws IOException;
}
