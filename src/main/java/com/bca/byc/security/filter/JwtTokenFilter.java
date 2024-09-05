package com.bca.byc.security.filter;

import com.bca.byc.enums.ErrorCode;
import com.bca.byc.response.ErrorResponseDTO;
import com.bca.byc.response.ErrorResponseFilter;
import com.bca.byc.security.util.JWTHeaderTokenExtractor;
import com.bca.byc.service.util.TokenBlacklistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.bca.byc.enums.ErrorCode.UNAUTHORIZED;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private JWTHeaderTokenExtractor jwtHeaderTokenExtractor;

    private static final String LOGIN_URL_APPS = "/api/auth/.*";
    private static final String PUBLIC_URL_APPS = "/api/v1/public/.*";
    private static final String LOGIN_URL_CMS = "/cms/auth/.*";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        // Skip token validation for the login URL
        if (requestURI.matches(LOGIN_URL_APPS) || requestURI.matches(PUBLIC_URL_APPS) || requestURI.matches(LOGIN_URL_CMS)) {
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
