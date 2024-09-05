package com.bca.byc.response;

import com.bca.byc.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseFilter {

    private String message;
    private ErrorCode errorCode;

}
