package com.bca.byc.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class FileUploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();

        // Set maximum file size (e.g., 10MB)
        factory.setMaxFileSize(DataSize.parse("10MB"));

        // Set maximum request size (e.g., 10MB)
        factory.setMaxRequestSize(DataSize.parse("10MB"));

        return factory.createMultipartConfig();
    }
}