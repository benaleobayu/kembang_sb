package com.bca.byc.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class PaginationResponse<T> implements Serializable {

    private boolean success;
    private String message;
    private Object data;

}
