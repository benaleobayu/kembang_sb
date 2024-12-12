package com.kembang.exception;

public class InvalidFileTypeImageException extends RuntimeException {
    public InvalidFileTypeImageException(String message) {
        super(message);
    }
}