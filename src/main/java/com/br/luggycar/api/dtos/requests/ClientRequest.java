package com.br.luggycar.api.dtos.requests;

import com.br.luggycar.api.enums.client.Gender;

import java.util.Date;

public record ClientRequest(
    String name,
    String lastName,
    String cpf,
    String email,
    Gender gender,
    Date dateBirth,
    String endereco
){}