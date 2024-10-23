package com.br.luggycar.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(ResourceDatabaseException.class)
    public ResponseEntity<String> handleDatabaseException(ResourceDatabaseException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
