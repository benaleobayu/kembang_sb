package com.bca.byc.config;

import com.bca.byc.exception.CustomAccessDeniedHandler;
import com.bca.byc.exception.CustomAuthenticationEntryPoint;
import com.bca.byc.security.filter.JwtAuthProcessingFilter;
import com.bca.byc.security.filter.JwtAuthenticationFilter;
import com.bca.byc.security.filter.JwtTokenFilter;
import com.bca.byc.security.filter.UsernamePasswordAuthProcessingFilter;
import com.bca.byc.security.handler.UsernamePasswordAuthFailureHandler;
import com.bca.byc.security.handler.UsernamePasswordAuthSucessHandler;
import com.bca.byc.security.provider.JwtAuthenticationProvider;
import com.bca.byc.security.provider.UsernamePasswordAuthProvider;
import com.bca.byc.security.util.JWTTokenFactory;
import com.bca.byc.security.util.SkipPathRequestMatcher;
import com.bca.byc.security.util.TokenExtractor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final static String AUTH_URL = "/v1/login";
    private final static String AUTH_URL_APPS = "/api/auth/**";
    private final static String PUBLIC_URL_APPS = "/api/v1/public/**";
    private final static String AUTH_URL_CMS = "/cms/auth/**";
    private final static String V1_URL = "/v1/**";
    private final static String V2_URL = "/v2/**";
    private final static String APPS_V1 = "/api/v1/**";
    private final static String CMS_V1 = "/cms/v1/**";

    private final static List<String> PERMIT_ENDPOINT_LIST = Arrays.asList(AUTH_URL, AUTH_URL_APPS, AUTH_URL_CMS, PUBLIC_URL_APPS,
            "/auth/login",
            "/static/**",
            "/uploads/**",
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/swagger-ui/index.css",
            "/favicon.ico",
            "/swagger-ui/swagger-ui.css",
            "/swagger-ui/swagger-ui.css.map",
            "/swagger-ui/swagger-ui-standalone-preset.js",
            "/swagger-ui/swagger-ui-standalone-preset.js.map",
            "/swagger-ui/swagger-ui-bundle.js",
            "/swagger-ui/swagger-ui-bundle.js.map",
            "/swagger-ui/favicon-32x32.png",
            "/swagger-ui/favicon-16x16.png",
            "/swagger-ui/swagger-initializer.js",
            "/v3/api-docs/swagger-config",
            "/v3/api-docs/**",
            "/api/chat/**",
            "/ws",
            "/ws/**",
            "/v3/api-docs"
    );
    private final static List<String> AUTHENTICATED_ENDPOINT_LIST = Arrays.asList(V1_URL, V2_URL, APPS_V1, CMS_V1);

    @Autowired
    private UsernamePasswordAuthProvider usernamePasswordAuthProvider;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public AuthenticationSuccessHandler usernamePasswordAuthSuccessHandler(ObjectMapper objectMapper, JWTTokenFactory jwtTokenFactory) {
        return new UsernamePasswordAuthSucessHandler(objectMapper, jwtTokenFactory);
    }

    @Bean
    public AuthenticationFailureHandler usernamePasswordAuthFailureHandler(ObjectMapper objectMapper) {
        return new UsernamePasswordAuthFailureHandler(objectMapper);
    }

    @Bean
    public CustomPermissionEvaluator permissionEvaluator() {
        return new CustomPermissionEvaluator();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(SecretKey jwtToken, ObjectMapper objectMapper) {
        return new JwtAuthenticationFilter(jwtToken, objectMapper);
    }

    @Autowired
    void registerProvider(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(usernamePasswordAuthProvider).authenticationProvider(jwtAuthenticationProvider);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public UsernamePasswordAuthProcessingFilter usernamePasswordAuthProcessingFilter(ObjectMapper objectMapper, AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler, AuthenticationManager manager) {
        UsernamePasswordAuthProcessingFilter filter = new UsernamePasswordAuthProcessingFilter(AUTH_URL, objectMapper, successHandler, failureHandler);
        filter.setAuthenticationManager(manager);
        return filter;
    }

    @Bean
    public JwtAuthProcessingFilter jwtAuthProcessingFilter(TokenExtractor tokenExtractor, AuthenticationFailureHandler failureHandler, AuthenticationManager authManager) {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(PERMIT_ENDPOINT_LIST, AUTHENTICATED_ENDPOINT_LIST);
        JwtAuthProcessingFilter filter = new JwtAuthProcessingFilter(matcher, tokenExtractor, failureHandler);
        filter.setAuthenticationManager(authManager);
        return filter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   UsernamePasswordAuthProcessingFilter usernamePasswordAuthProcessingFilter,
                                                   JwtAuthProcessingFilter jwtAuthProcessingFilter,
                                                   JwtTokenFilter jwtTokenFilter,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(PERMIT_ENDPOINT_LIST.toArray(new String[0])).permitAll()
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/uploads/**").permitAll()
                .requestMatchers("/api/chat/**").permitAll()
                .requestMatchers(V1_URL, V2_URL, APPS_V1, CMS_V1).authenticated());

        http.formLogin(formLogin -> formLogin
                .loginPage("/auth/login")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/swagger/index.html")
                .failureUrl("/auth/login?error=true")
                .usernameParameter("email")
                .permitAll());

        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler));


        http.sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JwtTokenFilter before authentication filters
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Add authentication filters
        http.addFilterBefore(usernamePasswordAuthProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthProcessingFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator());
        return expressionHandler;
    }


}
