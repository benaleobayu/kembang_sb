package com.kembang.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, IOException {
//        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
//                "unauthorized",
//                Collections.singletonList("Token expired or invalid"),
//                ErrorCode.UNAUTHORIZED,
//                HttpStatus.UNAUTHORIZED
//        );
//
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
//        response.getWriter().flush();
    }
}