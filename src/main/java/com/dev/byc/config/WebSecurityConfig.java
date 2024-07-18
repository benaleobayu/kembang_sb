package com.dev.byc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // http
        //     .csrf(csrf -> csrf.disable())
        //     .authorizeHttpRequests(authorize -> authorize
        //         .requestMatchers("/", "/home", "/login", "/css/**", "/js/**", "/images/**").permitAll()
        //         .requestMatchers("/api/auth/**").permitAll()
        //         .anyRequest().authenticated()
        //     )
        //     .formLogin(form -> form
        //         .loginPage("/login")
        //         .loginProcessingUrl("/perform_login")
        //         .defaultSuccessUrl("/home", true)
        //         .permitAll()
        //     )
        //     .logout(logout -> logout
        //         .logoutUrl("/perform_logout")
        //         .logoutSuccessUrl("/login")
        //         .permitAll()
        //     );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
