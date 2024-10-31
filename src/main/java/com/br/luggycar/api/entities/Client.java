package com.br.luggycar.api.entities;

import com.br.luggycar.api.enums.client.Gender;
import com.br.luggycar.api.enums.client.PersonType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private PersonType personType;
    private Date dateBirth;
    private String cep;
    private String endereco;
    private LocalDate registration;
    private String email;
    private String cpf;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String naturalPersonName;
    private String cnpj;
    private String companyName;

}
