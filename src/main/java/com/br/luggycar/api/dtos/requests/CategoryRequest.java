package com.br.luggycar.api.dtos.requests;


public record CategoryRequest(
        String name,
        String description,
        String image
) {
}