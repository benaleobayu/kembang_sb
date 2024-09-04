package com.bca.byc.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCode {

	INVALID_DATA(400),
	INTERNAL_ERROR(2),
	NETWORK_ERROR(3),
	OTHER_ERROR(4),
	DATA_NOT_FOUND(5),
	FORBIDDEN(403),
	UNAUTHORIZED(401),
	NOT_FOUND(8),
	CONFLICT(9),
	METHOD_NOT_ALLOWED(10),
	NOT_ACCEPTABLE(11),
	REQUEST_TIMEOUT(12),
	TOKEN_EXPIRED(401);

	private int code;
	ErrorCode(int code) {
		this.code=code;
	}

	@JsonValue
	public int code() {
		return code;
	}
}
