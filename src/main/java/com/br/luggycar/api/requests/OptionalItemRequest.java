package com.br.luggycar.api.requests;

public record OptionalItemRequest(
        String name,
        double rentalValue,
        double quantityAvailable
) {}
