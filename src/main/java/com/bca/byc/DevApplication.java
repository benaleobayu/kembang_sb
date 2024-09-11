package com.bca.byc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.bca.byc")
@EnableJpaRepositories(basePackages = "com.bca.byc.repository")
@EntityScan(basePackages = "com.bca.byc.entity")
public class DevApplication {
    public static void main(String[] args) {
        SpringApplication.run(DevApplication.class, args);
    }
}
