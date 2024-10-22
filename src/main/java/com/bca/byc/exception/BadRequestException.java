package com.bca.byc.exception;

import com.bca.byc.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpHeaders;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException{

	/**
	 *
	 */
	private static final long serialVersionUID = -174417453623224931L;

	public BadRequestException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ResponseEntity<ApiResponse> getResponseEntity(){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		return new ResponseEntity<>(new ApiResponse(false, getMessage()), headers, HttpStatus.BAD_REQUEST);
	}
}