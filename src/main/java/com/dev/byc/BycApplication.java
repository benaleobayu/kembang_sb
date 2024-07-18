package com.dev.byc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.dev.byc.repository")
@EntityScan(basePackages = "com.dev.byc.entity")
public class BycApplication {

    public static void main(String[] args) {
        SpringApplication.run(BycApplication.class, args);
    }
}
