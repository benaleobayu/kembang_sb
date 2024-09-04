package com.bca.byc.config;

import java.security.Key;

import javax.crypto.SecretKey;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bca.byc.security.util.JWTTokenFactory;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Configuration
public class AppConfig {

	@Value("${app.jwtSecret}")
	private String secret;

	@Value("${app.jwtExpirationInMs}")
	private int jwtExpirationMs;
	
	@Bean
	public SecretKey key() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	@Bean
	public JWTTokenFactory jwtTokenFactory(Key secret) {
		return new JWTTokenFactory(secret, jwtExpirationMs);
	}
 
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
//	@Bean
//	public ObjectMapper objectMapper() {
//		return new ObjectMapper();
//	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
 
}
