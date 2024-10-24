package com.br.luggycar.api.dtos.requests;

import com.br.luggycar.api.enums.client.Gender;
import com.br.luggycar.api.enums.client.PersonType;

import java.util.Date;

public record ClientRequest(
        PersonType personType,
        String naturalPersonName,
        String cpf,
        String cnpj,
        String companyName,
        String email,
        Gender gender,
        Date dateBirth,
        String cep,
        String endereco

) {}
