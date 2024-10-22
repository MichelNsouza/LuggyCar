package com.br.luggycar.api.dtos.requests;

public record OptionalItemRequest(
        String name,
        double rentalValue,
        double quantityAvailable
) {}
