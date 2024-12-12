package com.kembang.response;

import com.kembang.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseFilter {

    private String message;
    private ErrorCode errorCode;

}
