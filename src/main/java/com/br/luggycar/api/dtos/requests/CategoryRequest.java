package com.br.luggycar.api.dtos.requests;


import java.time.LocalDate;

public record CategoryRequest(
        String name,
        String description,
        String image,
        LocalDate registration
) {
}