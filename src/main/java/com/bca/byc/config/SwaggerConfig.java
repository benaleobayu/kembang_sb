package com.bca.byc.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.bca.byc.config.AppConfig.baseUrl;

@Configuration
@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT Authorization header using the Bearer scheme."
)
public class SwaggerConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server appServer = new Server();
        appServer.setUrl(baseUrl);
        appServer.setDescription("Development Server");

        return new OpenAPI()
                .servers(List.of(appServer));
    }

    @Bean
    public GroupedOpenApi appsApi() {
        return GroupedOpenApi.builder()
                .group("Apps_API")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi cmsApi() {
        return GroupedOpenApi.builder()
                .group("CMS_API")
                .pathsToMatch("/cms/**")
                .build();
    }
}
