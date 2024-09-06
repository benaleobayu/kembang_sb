package com.bca.byc.service.util;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenService {
    @Value("${app.jwtExpirationInMs}")
    private Long EXPIRATION_TIME; // 10 days
    private final SecretKey SECRET_KEY;

    public JwtTokenService(SecretKey secretKey) {
        SECRET_KEY = secretKey;
    }

    public String generateToken(String email, String role) {
        return Jwts.builder().subject(email)
                .claim("scopes", List.of(role))
                .issuer("https://bca.co.id")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)// Sign the token with the secret key
                .compact();
    }
}
