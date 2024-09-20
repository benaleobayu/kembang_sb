package com.bca.byc.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bca.byc.enums.ErrorCode;
import com.bca.byc.response.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.SecretKey;

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
                // Validasi token JWT
                Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwtToken);
                // Jika valid, Anda bisa menambahkan autentikasi ke SecurityContext
                // SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException ex) {
                List<String> details = new ArrayList<>();
                details.add("Token has expired. Please log in again.");

                ErrorResponseDTO errorResponse = ErrorResponseDTO.of("TOKEN EXPIRED", details, ErrorCode.TOKEN_EXPIRED, HttpStatus.FORBIDDEN);

                // Set response status and content type
                response.setStatus(HttpServletResponse.SC_FOUND);
                response.setContentType("application/json");

                // Write the ErrorResponseDTO to response as JSON
                String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                response.getWriter().write(jsonResponse);

                return; // Stop the filter chain after writing the response
            }
        }

        // Lanjutkan filter chain jika tidak ada masalah dengan JWT
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