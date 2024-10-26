package com.br.luggycar.api.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleException(ResourceNotFoundException ex) {
        Map<String, String> responseMessage = new HashMap<>();
        responseMessage.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
    }

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<Map<String, String>> handleException(ResourceExistsException ex) {
        Map<String, String> responseMessage = new HashMap<>();
        responseMessage.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
    }

    @ExceptionHandler(ResourceNullException.class)
    public ResponseEntity<Map<String, String>> handleException(ResourceNullException ex) {
        Map<String, String> responseMessage = new HashMap<>();
        responseMessage.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
    }

    @ExceptionHandler(ResourceDatabaseException.class)
    public ResponseEntity<String> handleDatabaseException(ResourceDatabaseException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(ResourceClientHasActiveRentalsException.class)
    public ResponseEntity<String> handleClientHasActiveRentalsException(ResourceClientHasActiveRentalsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

        for (ConstraintViolation<?> violation : violations) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
