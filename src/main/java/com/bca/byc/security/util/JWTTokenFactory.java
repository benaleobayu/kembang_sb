package com.bca.byc.security.util;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.AppUser;
import com.bca.byc.security.model.AccessJWTToken;
import com.bca.byc.service.AppAdminService;
import com.bca.byc.service.AppUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
public class JWTTokenFactory {

    private final Key secret;
    private final AppAdminService adminService;
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public AccessJWTToken createAccessJWTToken(String email, Collection<? extends GrantedAuthority> authorities) {
        authorities = authorities != null ? authorities : Collections.emptyList();
        Claims claims;
        if (authorities.isEmpty()) {
            claims = Jwts.claims().subject(email).build();
        } else {
            AppAdmin user = adminService.findByEmail(email);
            claims = Jwts.claims().subject(email)
                    .add("scopes", Arrays.asList(user.getRole().getName())).build();
        }

        //waktu kapan token dibuat
        LocalDateTime currentTime = LocalDateTime.now();
        Date currentTimeDate = Date.from(currentTime.atZone(ZoneId.of("Asia/Jakarta")).toInstant());

        //waktu kapan token expired
        LocalDateTime expiredTime = currentTime.plusMinutes(24 * 60);
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

    public AppAdmin parseToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) secret)
                .build().parseSignedClaims(token)
                .getPayload();
        return adminService.findByEmail(claims.getSubject());
    }
}
