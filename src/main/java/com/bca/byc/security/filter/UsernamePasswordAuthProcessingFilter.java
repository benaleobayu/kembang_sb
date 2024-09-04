package com.bca.byc.security.filter;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bca.byc.model.LoginRequestDTO;
import com.bca.byc.exception.BadRequestException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthProcessingFilter extends AbstractAuthenticationProcessingFilter{
	
	private final ObjectMapper objectMapper;
	
	private final AuthenticationSuccessHandler successHandler;
	
	private final AuthenticationFailureHandler failureHandler;

	public UsernamePasswordAuthProcessingFilter(String defaultFilterProcessesUrl, ObjectMapper objectMapper, AuthenticationSuccessHandler successHandler, 
			AuthenticationFailureHandler failureHandler) {
		super(defaultFilterProcessesUrl);
		this.objectMapper = objectMapper;
		this.successHandler = successHandler;
		this.failureHandler = failureHandler;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		LoginRequestDTO dto = objectMapper.readValue(request.getReader(), LoginRequestDTO.class);
		if(StringUtils.isBlank(dto.email()) || StringUtils.isBlank(dto.password())) {
			throw new BadRequestException("email.password.shouldbe.provided");
		}
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
		return this.getAuthenticationManager().authenticate(token);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		this.successHandler.onAuthenticationSuccess(request, response, authResult);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		this.failureHandler.onAuthenticationFailure(request, response, failed);
	}
	
	

}
