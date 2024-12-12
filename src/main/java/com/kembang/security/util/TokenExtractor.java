package com.kembang.security.util;

public interface TokenExtractor {
	
	String extract(String payload);

	String getEmail(String token);

}
