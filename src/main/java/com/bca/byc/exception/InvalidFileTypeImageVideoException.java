package com.bca.byc.exception;

public class InvalidFileTypeImageVideoException extends RuntimeException {
    public InvalidFileTypeImageVideoException(String message) {
        super(message);
    }
}