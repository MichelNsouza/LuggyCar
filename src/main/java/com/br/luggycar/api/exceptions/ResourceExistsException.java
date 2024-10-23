package com.br.luggycar.api.exceptions;

public class ResourceExistsException extends RuntimeException{

    public ResourceExistsException(String message) {
        super(message);
    }
}
