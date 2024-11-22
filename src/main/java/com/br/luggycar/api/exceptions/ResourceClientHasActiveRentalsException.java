package com.br.luggycar.api.exceptions;

public class ResourceClientHasActiveRentalsException extends Exception {
    public ResourceClientHasActiveRentalsException(String message) {
        super(message);
    }
}
