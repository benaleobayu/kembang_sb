package com.bca.byc.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;


@Component
public class JWTHeaderTokenExtractor implements TokenExtractor {

	private static final String HEADER_PREFIX = "Bearer ";
	private final SecretKey key;

    public JWTHeaderTokenExtractor(SecretKey key) {
        this.key = key;
    }

    @Override
	public String extract(String payload) {
//		if (StringUtils.isBlank(payload)) {
//			throw new AuthenticationServiceException("Authorization header should be provided");
//		}
//
//		if (payload.length() < HEADER_PREFIX.length()) {
//			throw new AuthenticationServiceException("Invalid authorization header");
//		}

//		return payload.substring(HEADER_PREFIX.length());
		if (payload != null && payload.startsWith("Bearer ")) {
			return payload.substring(7); // Extract the token
		}
		return null;
	}

	@Override
	public String getEmail(String token) {
		try{
			Claims claims = Jwts.parser().verifyWith(key)
					.build().parseSignedClaims(token).getPayload();
			return claims.getSubject();
		} catch (JwtException | IllegalArgumentException e){
			// Log the exception for debugging purposes
			System.err.println("Error extracting email from token: " + e.getMessage());
			return null;
		}
	}
}