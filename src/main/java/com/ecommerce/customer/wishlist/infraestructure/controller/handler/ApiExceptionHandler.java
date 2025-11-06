package com.ecommerce.customer.wishlist.infraestructure.controller.handler;

import com.ecommerce.customer.wishlist.domain.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class ApiExceptionHandler {

    private static final String ERROR = "error";

        @ExceptionHandler(WishlistLimitExceededException.class)
        public ResponseEntity<Map<String, String>> handleWishlistLimit(WishlistLimitExceededException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(ERROR, ex.getMessage()));
        }

        @ExceptionHandler(ProductAlreadyExistsException.class)
        public ResponseEntity<Map<String, String>> handleProductExists(ProductAlreadyExistsException ex) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(ERROR, ex.getMessage()));
        }

        @ExceptionHandler(ProductNotFoundException.class)
        public ResponseEntity<Map<String, String>> handleProductNotFound(ProductNotFoundException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERROR, ex.getMessage()));
        }

        @ExceptionHandler(ClientNotFoundException.class)
        public ResponseEntity<Map<String, String>> handleClientNotFound(ClientNotFoundException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERROR, ex.getMessage()));
        }

        @ExceptionHandler(WishListNotFoundException.class)
        public ResponseEntity<Map<String, String>> handleWishlistNotFound(WishListNotFoundException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERROR, ex.getMessage()));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(Map.of(ERROR, INTERNAL_SERVER_ERROR.getReasonPhrase()));
        }

    @ExceptionHandler({IllegalArgumentException.class, ConstraintViolationException.class})
    public ResponseEntity<Map<String, String>> handleInvalidInput(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(ERROR, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
            var fieldName = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField())
                .orElse("field");

        var errorMessage = "The field '" + fieldName + "' is required and cannot be null";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", errorMessage));
    }
}