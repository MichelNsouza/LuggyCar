package com.br.luggycar.api.entities;

import com.br.luggycar.api.enums.client.Gender;
import com.br.luggycar.api.enums.client.PersonType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NotNull
    @Enumerated(EnumType.STRING)
    private PersonType personType;

    private String naturalPersonName;
    private String cpf;

    private String cnpj;
    private String companyName;

    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Date dateBirth;

    private String cep;
    private String endereco;

    private LocalDate registration;
}
