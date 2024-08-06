package com.bca.byc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

@Configuration
public class AppConfig {

    @Bean
    public StopWatch stopWatch() {
        return new StopWatch();
    }
}
