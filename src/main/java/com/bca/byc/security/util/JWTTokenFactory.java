package com.bca.byc.security.util;

import com.bca.byc.security.model.AccessJWTToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JWTTokenFactory {

    private final Key secret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public AccessJWTToken createAccessJWTToken(String email, Collection<? extends GrantedAuthority> authorities) {
        Claims claims = Jwts.claims().subject(email)
                .add("scopes", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).build();

        //waktu kapan token dibuat
        LocalDateTime currentTime = LocalDateTime.now();
        Date currentTimeDate = Date.from(currentTime.atZone(ZoneId.of("Asia/Jakarta")).toInstant());

        //waktu kapan token expired
        LocalDateTime expiredTime = currentTime.plusMinutes(24*60);
        Date expiredTimeDate = Date.from(expiredTime.atZone(ZoneId.of("Asia/Jakarta")).toInstant());

        String token = Jwts.builder().claims(claims)
                .issuer("https://bca.co.id")
                .issuedAt(currentTimeDate)
                .expiration(expiredTimeDate)
                .signWith(secret).compact();

        return new AccessJWTToken(token, claims);
    }

    public long getExpirationTime() {
        return jwtExpirationInMs;
    }


    public void invalidateToken(String token) {
        try {
            // Parse and verify the token (optional, for extra validation)
            Jwts.parser()
                    .verifyWith((SecretKey) secret)
                    .build()
                    .parseSignedClaims(token);

            // Add the token to the blacklist
            blacklistToken(token);
        } catch (Exception e) {
            // Handle token parsing exceptions (e.g., expired, invalid token)
            throw new RuntimeException("Invalid token", e);
        }
    }

    private void blacklistToken(String token) {
        System.out.println("Token: " + token);
    }
}
