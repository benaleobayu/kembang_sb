package com.bca.byc.exception;

import com.bca.byc.enums.ErrorCode;
import com.bca.byc.response.ApiResponse;
import com.bca.byc.response.ErrorResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String message = "The media type " + ex.getContentType() + " is not supported.";
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<>(new ApiResponse(false, message), headers, status);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of("data not found", details, ErrorCode.DATA_NOT_FOUND, HttpStatus.BAD_REQUEST);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<ErrorResponseDTO> handleUnauthorizedException(ResourceNotFoundException ex, WebRequest request) {
        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of("Unauthorized", details, ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(ForbiddenException.class)
    protected ResponseEntity<ErrorResponseDTO> handleForbiddenException(ResourceNotFoundException ex, WebRequest request) {
        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of("FORBIDDEN", details, ErrorCode.FORBIDDEN, HttpStatus.FORBIDDEN);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<ErrorResponseDTO> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        // Add this logging statement
        System.out.println("ExpiredJwtException handled: " + ex.getMessage());

        List<String> details = new ArrayList<>();
        details.add("Token has expired. Please log in again.");
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of("TOKEN EXPIRED", details, ErrorCode.TOKEN_EXPIRED, HttpStatus.FOUND);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        return ResponseEntity.status(HttpStatus.FOUND).body(errorResponse);
    }

    // Exception handler for other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneralException(Exception ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");

        return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), headers, HttpStatus.INTERNAL_SERVER_ERROR);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<>(new ApiResponse(false, "File size exceeds the maximum limit!"), headers, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<>(new ApiResponse(false, "Internal Server Error"), headers, statusCode);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> details = new ArrayList<String>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of("invalid data", details, ErrorCode.INVALID_DATA, HttpStatus.BAD_REQUEST);
        headers.add("Access-Control-Allow-Origin", "*");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*"); // Menambahkan header CORS

        return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), headers, HttpStatus.BAD_REQUEST);
    }


}
