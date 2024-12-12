package com.kembang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.kembang")
@EnableJpaRepositories(basePackages = "com.kembang.repository")
@EntityScan(basePackages = "com.kembang.entity")
@EnableScheduling
public class DevApplication {

    public static void main(String[] args) throws Exception {

//        SshTunnelUtil.setupSshTunnel("root", "45.32.122.20", 22, "8kF*b*6jDH3buK69", "localhost", 5432, 5432);
//        SshTunnelUtil.setupSshTunnel("root", "45.32.122.20", 22, "8kF*b*6jDH3buK69", "localhost", 9200, 9200);

        SpringApplication.run(DevApplication.class, args);
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//
//        corsConfiguration.setAllowedOrigins(Arrays.asList(
//                "https://admin-byc2024.kelolain.id",
//                "http://localhost:4200"
//        ));
//
//        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
//        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
//        corsConfiguration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration);
//
//        return source;
//    }


}
