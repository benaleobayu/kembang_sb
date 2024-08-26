package com.bca.byc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ResourceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5191573893376153070L;

    public ResourceNotFoundException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

}
