package com.kembang.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCode {

	INVALID_DATA(400),
	INTERNAL_ERROR(2),
	NETWORK_ERROR(500),
	OTHER_ERROR(999),
	DATA_NOT_FOUND(404),
	FORBIDDEN(403),
	UNAUTHORIZED(401),
	NOT_FOUND(404),
	CONFLICT(9),
	METHOD_NOT_ALLOWED(10),
	NOT_ACCEPTABLE(11),
	REQUEST_TIMEOUT(12),
	TOKEN_EXPIRED(404),
	REDIRECT(302),

	// BCA CUSTOM CODE
	unsupported_grant_type(400),
	invalid_request(400),
	Invalid_client(400),
	unauthorized_client(401),
	invalid_grant(400);

	private int code;
	ErrorCode(int code) {
		this.code=code;
	}

	@JsonValue
	public int code() {
		return code;
	}
}
