package com.br.luggycar.api.entities;

import com.br.luggycar.api.enums.client.Gender;
import jakarta.persistence.*;
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