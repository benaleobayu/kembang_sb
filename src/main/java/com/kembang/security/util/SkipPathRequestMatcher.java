package com.kembang.security.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.servlet.http.HttpServletRequest;

public class SkipPathRequestMatcher implements RequestMatcher {

	private final OrRequestMatcher skipMatcher;

	private final OrRequestMatcher processingMatcher;

	public SkipPathRequestMatcher(List<String> pathToSkip, List<String> processingPath) {
		List<RequestMatcher> skip = pathToSkip.stream().map(AntPathRequestMatcher::new)
				.collect(Collectors.toList());
		skipMatcher = new OrRequestMatcher(skip);
		
		List<RequestMatcher> processing = processingPath.stream().map(AntPathRequestMatcher::new)
				.collect(Collectors.toList());
		processingMatcher= new OrRequestMatcher(processing);
	}

	@Override
	public boolean matches(HttpServletRequest request) {
		if(skipMatcher.matches(request)) {
			return false;
		}
		return processingMatcher.matches(request);
	}

}
