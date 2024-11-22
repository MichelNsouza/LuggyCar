package com.br.luggycar.api.dtos.requests.Optional;

public record OptionalItemRequest(
        String name,
        double rentalValue,
        double quantityAvailable
) {
}
