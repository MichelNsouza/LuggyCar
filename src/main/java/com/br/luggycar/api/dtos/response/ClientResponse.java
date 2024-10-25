package com.br.luggycar.api.dtos.response;

import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.enums.client.Gender;
import com.br.luggycar.api.enums.client.PersonType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZoneId;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClientResponse(
        Long id,
        PersonType personType,
        String naturalPersonName,
        String cpf,
        String cnpj,
        String companyName,
        String email,
        Gender gender,
        LocalDate dateBirth,
        String cep,
        String endereco,
        LocalDate registration

) {
    public ClientResponse(Client client) {
        this(
                client.getId(),
                client.getPersonType(),
                client.getNaturalPersonName(),
                client.getCpf(),
                client.getCnpj(),
                client.getCompanyName(),
                client.getEmail(),
                client.getGender(),
                client.getDateBirth() != null ? client.getDateBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null,
                client.getCep(),
                client.getEndereco(),
                client.getRegistration()
        );
    }
}
