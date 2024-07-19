package com.dev.byc.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import  com.dev.byc.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/home", "/login", "/falcon/**", "/assets/**", "/images/**").permitAll()
                .requestMatchers("/cms/dashboard").authenticated() // Requires login for /cms/dashboard
                .anyRequest().authenticated() // Ensures all other requests require authentication
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login") // Specify the custom login page URL
                .loginProcessingUrl("/perform_login") // URL to submit the email and password
                .defaultSuccessUrl("/cms/dashboard", true) // Redirect to dashboard on successful login
                .failureUrl("/login?error=true") // Redirect to login page with error parameter on failure
                .usernameParameter("email") // Set the parameter name for the email
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/perform_logout")
                .logoutSuccessUrl("/login?logout=true")
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .sessionManagement(sessionManagement -> 
                sessionManagement
                    .sessionFixation().migrateSession()
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                   .userDetailsService(userDetailsService)
                   .passwordEncoder(passwordEncoder())
                   .and()
                   .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
