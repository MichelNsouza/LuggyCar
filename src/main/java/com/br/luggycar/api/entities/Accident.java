package com.br.luggycar.api.entities;

import com.br.luggycar.api.enums.accident.Severity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Accident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Severity severity;


    @NotNull
    @Column(unique = true)
    private String description;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")  // Nome da coluna que será a chave estrangeira
    @JsonBackReference  // Evita o loop infinito de serialização JSON
    private Vehicle vehicle;

    private Date registrationDate;
}
