package com.bca.byc.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCode {

    INVALID_DATA(1),
        INTERNAL_ERROR(2),
            NETWORK_ERROR(3),
                OTHER_ERROR(4),
                    UNAUTHORIZED(5),
                        FORBIDDEN(6),
                            NOT_FOUND(7),
                                BAD_REQUEST(8),
                                    METHOD_NOT_ALLOWED(9),
                                        INTERNAL_SERVER_ERROR(10),
                                            SERVICE_UNAVAILABLE(11),
                                                DATA_NOT_FOUND(12),;

    private final int code;
    ErrorCode(int code) {
        this.code=code;
    }

    @JsonValue
    public int code(){
        return code;
    }
}
