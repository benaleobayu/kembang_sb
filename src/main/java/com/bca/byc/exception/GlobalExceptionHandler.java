package com.bca.byc.exception;

import com.bca.byc.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach(error -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Resource not found: " + e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse> handleGenericException(Exception e) {
//        if (e instanceof ConstraintViolationException) {
//            ConstraintViolationException ex = (ConstraintViolationException) e;
//
//            Map<String, String> errors = new HashMap<>();
//            ex.getConstraintViolations().forEach(violation -> {
//                String propertyPath = violation.getPropertyPath().toString();
//                String message = violation.getMessage();
//                errors.put(propertyPath, message);
//            });
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ApiResponse(false, errors.toString()));
//        }
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(new ApiResponse(false, "An error occurred: " + e.getMessage()));
//    }

    // combining
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        StringBuilder errorMessage = new StringBuilder();

        ex.getConstraintViolations().forEach(violation -> {
            String message = violation.getMessage();
            errorMessage.append(message).append("; ");
        });

        // Remove the last "; " from the error message
        String finalErrorMessage = errorMessage.toString().trim();
        if (finalErrorMessage.endsWith(";")) {
            finalErrorMessage = finalErrorMessage.substring(0, finalErrorMessage.length() - 1);
        }

        return ResponseEntity.badRequest().body(new ApiResponse(false, finalErrorMessage));
    }

//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse> handleGenericException(Exception e) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "An error occurred: " + e.getMessage()));
//    }

}