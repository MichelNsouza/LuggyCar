package com.br.luggycar.api.dtos.requests;

import com.br.luggycar.api.enums.client.Gender;
import com.br.luggycar.api.enums.client.PersonType;
import com.br.luggycar.api.validators.annotation.LegalAge;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

public record ClientRequest(

        PersonType personType,

        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
        String naturalPersonName,
        @CPF(message = "CPF inválido")
        String cpf,
        @LegalAge
        @Past(message = "A data de nascimento deve estar no passado.")
        Date dateBirth,
        Gender gender,

        @CNPJ(message = "CNPJ inválido")
        String cnpj,
        @Size(min = 2, max = 150, message = "O nome deve ter entre 2 e 150 caracteres.")
        String companyName,

        @Email(message = "Email inválido")
        String email,
        String cep,
        @Size(min = 5, max = 300, message = "O endereço deve ter entre 3 e 300 caracteres.")
        String endereco

) {
    public ClientRequest {

    if (cpf != null) {
        cpf = cpf.replace(".", "").replace("-", "");
        cnpj = null;
        companyName = null;
    }

    if (cnpj != null) {
        cnpj = cnpj.replace(".", "").replace("-", "").replace("/", "");
        cpf = null;
        gender = null;
        }
    }

}
