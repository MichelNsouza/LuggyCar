package com.br.luggycar.api.entities;

import com.br.luggycar.api.enums.client.Gender;
import com.br.luggycar.api.enums.client.PersonType;
import com.br.luggycar.api.enums.client.licenseCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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

    private String drivers_license_number;

    @Future(message = "A validade deve ser no futuro")
    private Date drivers_license_validity;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    @CollectionTable(name = "driver_license_categories", joinColumns = @JoinColumn(name = "driver_id"))
    @Column(name = "license_category")
    private List<licenseCategory> drivers_license_category;
}
