package com.bca.byc;

import com.bca.byc.util.SshTunnelUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication(scanBasePackages = "com.bca.byc")
@EnableJpaRepositories(basePackages = "com.bca.byc.repository")
@EnableElasticsearchRepositories(basePackages = "com.bca.byc.repository.Elastic")
@EntityScan(basePackages = "com.bca.byc.entity")
//@EnableScheduling
public class DevApplication {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @Value("${spring.servlet.multipart.max-request-size}")
    private String maxRequestSize;


    public static void main(String[] args) throws Exception {

//        SshTunnelUtil.setupSshTunnel("root", "45.32.122.20", 22, "8kF*b*6jDH3buK69", "localhost", 5432, 5432);
//        SshTunnelUtil.setupSshTunnel("root", "45.32.122.20", 22, "8kF*b*6jDH3buK69", "localhost", 9200, 9200);

        SpringApplication.run(DevApplication.class, args);
    }

    @PostConstruct
    public void init() {
        System.out.println("spring.servlet.multipart.max-file-size: " + maxFileSize);
        System.out.println("spring.servlet.multipart.max-request-size: " + maxRequestSize);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList(
                "https://admin-byc2024.kelolain.id",
                "http://localhost:4200",
                "http://localhost",
                "http://localhost/"

        ));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"));
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
