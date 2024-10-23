package com.br.luggycar.api.dtos.response;

import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.enums.client.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;
import java.util.Date;

public record ClientResponse(
        Long id,
        String name,
        String cpf,
        String email,
        LocalDate registration
) {
    public ClientResponse(Client client){
        this(
                client.getId(),
                client.getName(),
                client.getCpf(),
                client.getEmail(),
                client.getRegistration()
        );
    }
}
