package com.bca.byc.service.security;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtTokenService {
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days
    private final SecretKey SECRET_KEY;

    public JwtTokenService(SecretKey secretKey) {
        SECRET_KEY = secretKey;
    }

    public String generateToken(String email) {
        return Jwts.builder().subject(email).expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY) // Sign the token with the secret key
                .compact();
    }
}
