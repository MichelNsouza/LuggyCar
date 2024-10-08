package com.br.luggycar.api.requests;

import com.br.luggycar.api.enums.user.UserRole;

public record  RegisterRequest (
        String login,
        String password,
        UserRole role
){}