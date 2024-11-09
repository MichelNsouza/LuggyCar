package com.br.luggycar.api.exceptions;

public class ResourceBadRequestException extends RuntimeException{

    public ResourceBadRequestException(String message) {
        super(message);
    }
}