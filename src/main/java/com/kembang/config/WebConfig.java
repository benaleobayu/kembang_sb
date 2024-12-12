package com.kembang.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.base.url}")
    private String baseUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (baseUrl.contains("localhost")) {
            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations("classpath:/static/uploads/");
        } else {
            // Serve from the filesystem
            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations("file:/var/www/html/cms-byc2024/uploads/");
        }


    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("https://admin-byc2024.kelolain.id")
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }
}
