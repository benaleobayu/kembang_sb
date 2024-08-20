package com.bca.byc.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT Authorization header using the Bearer scheme."
)
public class SwaggerConfig {

    @Value("${app.base.url}")
    private String baseUrl;

    @Bean
    public OpenAPI myOpenAPI(){

        Server appServer = new Server();
        appServer.setUrl(baseUrl);
        appServer.description("Development Server");

        return new OpenAPI()
                .servers(List.of(appServer));

    }
}
