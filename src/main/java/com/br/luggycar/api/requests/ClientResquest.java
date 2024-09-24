package com.br.luggycar.api.requests;

import com.br.luggycar.api.entities.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientResquest {

    private String name;
    private String lastName;
    private String cpf;
    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Date dateBirth;
    private String endereco;
    private LocalDate registration;
}