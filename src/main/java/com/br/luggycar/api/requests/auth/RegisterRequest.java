package com.br.luggycar.api.requests.auth;

import com.br.luggycar.api.entities.user.UserRole;

public record  RegisterRequest (String login, String password, UserRole role) {
}