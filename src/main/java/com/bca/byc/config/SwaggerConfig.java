package com.bca.byc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${app.dev.url}")
    private String localUrl;

    @Value("${app.base.url}")
    private String devUrl;



    @Bean
    public OpenAPI myOpenAPI(){
        Server localServer = new Server();
        localServer.setUrl(localUrl);
        localServer.description("Local Server");

        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.description("Development Server");


        return new OpenAPI()
                .servers(List.of(localServer,devServer));

    }
}
