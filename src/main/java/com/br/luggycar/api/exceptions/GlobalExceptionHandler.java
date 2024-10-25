package com.br.luggycar.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleException(ResourceNotFoundException ex) {
        Map<String, String> responseMessage = new HashMap<>();
        responseMessage.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
    }

//    @ExceptionHandler(ResourceExistsException.class)
//    public ResponseEntity<Map<String, String>> handleException(ResourceExistsException ex) {
//        Map<String, String> responseMessage = new HashMap<>();
//        responseMessage.put("message", ex.getMessage());
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
//    }
//
//    @ExceptionHandler(ResourceNullException.class)
//    public ResponseEntity<Map<String, String>> handleException(ResourceNullException ex) {
//        Map<String, String> responseMessage = new HashMap<>();
//        responseMessage.put("message", ex.getMessage());
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
//    }

    @ExceptionHandler({ResourceExistsException.class, ResourceNullException.class})
    public ResponseEntity<Map<String, Object>> handleResourceExceptions(RuntimeException ex) {
        Map<String, Object> responseMessage = new HashMap<>();
        List<String> errorMessages = new ArrayList<>();

        // Se a exceção for ResourceNullException, adicione suas mensagens
        if (ex instanceof ResourceNullException) {
            errorMessages.add(ex.getMessage()); // Adiciona a mensagem da exceção
        } else {
            // Adiciona a mensagem da exceção à lista de erros para ResourceExistsException
            errorMessages.add(ex.getMessage());
        }
        responseMessage.put("messages", errorMessages);

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
}
