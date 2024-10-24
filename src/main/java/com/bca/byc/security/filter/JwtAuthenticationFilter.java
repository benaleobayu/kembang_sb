package com.bca.byc.security.filter;

import com.bca.byc.enums.ErrorCode;
import com.bca.byc.response.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final SecretKey secretKey;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwtToken = extractJwtFromRequest(request);

        if (jwtToken != null) {
            try {
                Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwtToken);
            } catch (ExpiredJwtException ex) {
                List<String> details = new ArrayList<>();
                details.add("Token has expired. Please log in again.");

                ErrorResponseDTO errorResponse = null;

                if (request.getRequestURI().contains("cms/")) {
                    errorResponse = ErrorResponseDTO.of("Please re-login your account", details, ErrorCode.REDIRECT, HttpStatus.FORBIDDEN);
                    response.setStatus(HttpServletResponse.SC_FOUND); // 302
                } else if (request.getRequestURI().contains("api/")) {
                    errorResponse = ErrorResponseDTO.of("Please re-login your account", details, ErrorCode.UNAUTHORIZED, HttpStatus.FORBIDDEN);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                }
                response.setContentType("application/json");

                String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                response.getWriter().write(jsonResponse);

                return; // Stop the filter chain after writing the response
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}