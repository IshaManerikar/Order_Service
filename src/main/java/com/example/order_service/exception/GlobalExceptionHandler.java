package com.example.order_service.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.user_service.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - Not Found
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleOrderNotFound(
            OrderNotFoundException ex) {

        ApiResponse<Void> response =
                new ApiResponse<>(ex.getMessage(), 404, null);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 400 - Bad Request (optional for now)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(
            IllegalArgumentException ex) {

        ApiResponse<Void> response =
                new ApiResponse<>(ex.getMessage(), 400, null);

        return ResponseEntity.badRequest().body(response);
    }

    // 500 - Internal Server Error (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(
            Exception ex) {

        ApiResponse<Void> response =
                new ApiResponse<>("Internal server error", 500, null);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
          .getFieldErrors()
          .forEach(error ->
              errors.put(error.getField(), error.getDefaultMessage())
          );

        ApiResponse<Map<String, String>> response =
                new ApiResponse<>(
                        "Validation failed",
                        400,
                        errors
                );

        return ResponseEntity.badRequest().body(response);
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleJsonParseError(
            HttpMessageNotReadableException ex) {

        ApiResponse<String> response =
                new ApiResponse<>(
                        "Invalid request body. Please check data types.",
                        400,
                        null
                );

        return ResponseEntity.badRequest().body(response);
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(ex.getMessage(), 400, null));
    }
    
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ApiResponse<Object>> handleWebClientException(
            WebClientResponseException ex) {

        ApiResponse<Object> response =
                new ApiResponse<>(
                        ex.getResponseBodyAsString(),
                        ex.getStatusCode().value(),
                        null
                );

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(response);
    }
    
    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidOrderStatus(
            InvalidOrderStatusException ex) {

        return ResponseEntity.badRequest().body(
            new ApiResponse<>(
                ex.getMessage(),
                400,
                null
            )
        );
    }




}
