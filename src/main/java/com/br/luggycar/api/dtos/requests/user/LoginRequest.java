package com.br.luggycar.api.dtos.requests.user;

public record LoginRequest(
        String login,
        String password
){}
