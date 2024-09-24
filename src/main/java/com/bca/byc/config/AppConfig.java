package com.bca.byc.config;

import com.bca.byc.security.util.JWTTokenFactory;
import com.bca.byc.service.AppAdminService;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import java.security.Key;


@Configuration
public class AppConfig {

    @Value("${app.jwtSecret}")
    private String secret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationMs;

    @Value("${app.base.url}")
    private String baseUrl;

    @Autowired
    private AppAdminService adminService;

    @Bean
    public SecretKey key() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Bean
    public JWTTokenFactory jwtTokenFactory(Key secret) {
        return new JWTTokenFactory(secret, adminService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Bean
    public String baseUrl() {
        return baseUrl;
    }

    @Bean
    public Integer jwtExpirationMs() {
        return jwtExpirationMs;
    }

}
