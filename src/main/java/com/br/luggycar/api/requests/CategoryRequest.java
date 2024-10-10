package com.br.luggycar.api.requests;


public record CategoryRequest(
        String name,
        String description,
        String image
) {
}