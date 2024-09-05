package com.bca.byc.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PostService {

    String uploadContent(MultipartFile fileName) throws IOException;

    public byte[] downloadContent(String fileName) throws IOException;

}
