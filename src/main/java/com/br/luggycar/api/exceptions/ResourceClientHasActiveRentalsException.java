package com.br.luggycar.api.exceptions;

public class ResourceClientHasActiveRentalsException extends RuntimeException {
    public ResourceClientHasActiveRentalsException(String message) {
        super(message);
    }
}
