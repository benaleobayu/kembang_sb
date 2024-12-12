package com.kembang.exception;

public class InvalidFileTypeImageVideoException extends RuntimeException {
    public InvalidFileTypeImageVideoException(String message) {
        super(message);
    }
}