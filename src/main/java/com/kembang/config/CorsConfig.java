package com.kembang.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsConfig {

    @Value("${app.frontend.url}")
    private String frontendUrl;

//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowedOrigins(Arrays.asList(
//                frontendUrl,
//                "http://localhost:3000"
//        ));
//        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
//        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
//        corsConfiguration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration);
//
//        return new CorsFilter(source);
//    }

}