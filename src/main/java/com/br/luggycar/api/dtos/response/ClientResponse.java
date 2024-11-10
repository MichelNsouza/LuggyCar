package com.br.luggycar.api.dtos.response;

import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.enums.client.Gender;
import com.br.luggycar.api.enums.client.PersonType;
import com.br.luggycar.api.enums.client.licenseCategory;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZoneId;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClientResponse(
        Long id,
        PersonType personType,
        String naturalPersonName,
        String cpf,
        String cnpj,
        String companyName,
        String email,
        String drivers_license_number,
        Date drivers_license_validity,
        List<licenseCategory> drivers_license_category,
        Gender gender,
        Date dateBirth,
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
                client.getDrivers_license_number(),
                client.getDrivers_license_validity(),
                client.getDrivers_license_category(),
                client.getGender(),
                client.getDateBirth(),
                client.getCep(),
                client.getEndereco(),
                client.getRegistration()
        );
    }
}
