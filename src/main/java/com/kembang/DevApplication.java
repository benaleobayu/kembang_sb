package com.kembang;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication(scanBasePackages = "com.kembang")
@EnableJpaRepositories(basePackages = "com.kembang.repository")
@EntityScan(basePackages = "com.kembang.entity")
@EnableScheduling
public class DevApplication {

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public static void main(String[] args) throws Exception {

//        SshTunnelUtil.setupSshTunnel("root", "45.32.122.20", 22, "8kF*b*6jDH3buK69", "localhost", 5432, 5432);
//        SshTunnelUtil.setupSshTunnel("root", "45.32.122.20", 22, "8kF*b*6jDH3buK69", "localhost", 9200, 9200);

        SpringApplication.run(DevApplication.class, args);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOrigins(Arrays.asList(
                frontendUrl,
                "http://localhost:4200"
        ));

        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }


}
