package com.bca.byc.dto;

import com.bca.byc.enums.ErrorCode;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ErrorResponse implements Serializable {

    private Date timestamp;
    private String message;
    private ErrorCode errorCode;
    private List<String> details;
    private HttpStatus status;

    public ErrorResponse(String message, ErrorCode errorCode, List<String> details, HttpStatus status) {
        this.message = message;
        this.errorCode = errorCode;
        this.details = details;
        this.status = status;
        this.timestamp= new Date();
    }

    public static ErrorResponse of (final String message, List<String> details, final ErrorCode errorCode, final HttpStatus status) {
        return new ErrorResponse(message, errorCode, details, status);
    }

}

