package com.bca.byc.security.filter;

import com.bca.byc.response.ErrorResponseDTO;
import com.bca.byc.security.util.JWTHeaderTokenExtractor;
import com.bca.byc.service.util.TokenBlacklistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static com.bca.byc.enums.ErrorCode.UNAUTHORIZED;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String LOGIN_URL_APPS = "/api/auth/.*";
    private static final String PUBLIC_URL_APPS = "/api/v1/public/.*";
    private static final String LOGIN_URL_CMS = "/cms/auth/.*";
    private static final Set<String> PERMIT_ENDPOINT_LIST = Set.of(LOGIN_URL_APPS, PUBLIC_URL_APPS, LOGIN_URL_CMS,
            "/static/.*",
            "/uploads/.*",
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
            "/v3/api-docs/.*",
            "/ws",
            "/ws/.*",
            "/api/chat/.*",
            "/v3/api-docs");
    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    @Autowired
    private JWTHeaderTokenExtractor jwtHeaderTokenExtractor;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Handle the JWT token validation for other requests
        String requestURI = request.getRequestURI();
        // Skip token validation for the login URL
        if (PERMIT_ENDPOINT_LIST.stream().anyMatch(requestURI::matches)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtHeaderTokenExtractor.extract(request.getHeader("Authorization"));

        if (token != null && !tokenBlacklistService.isTokenBlacklisted(token)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ErrorResponseDTO errorResponse = new ErrorResponseDTO("Unauthorized", UNAUTHORIZED, List.of("Unauthorized"), HttpStatus.UNAUTHORIZED);
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        }
    }
}
