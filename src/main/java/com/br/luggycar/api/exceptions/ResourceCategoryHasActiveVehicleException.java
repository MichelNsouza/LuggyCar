package com.br.luggycar.api.exceptions;


public class ResourceCategoryHasActiveVehicleException extends RuntimeException {
    public ResourceCategoryHasActiveVehicleException(String message) {
        super(message);
    }
}