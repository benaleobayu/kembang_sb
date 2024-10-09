package com.bca.byc.exception;

public class InvalidFileTypeImageException extends RuntimeException {
    public InvalidFileTypeImageException(String message) {
        super(message);
    }
}