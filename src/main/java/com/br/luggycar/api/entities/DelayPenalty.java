package com.br.luggycar.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DelayPenalty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ou GenerationType.AUTO, conforme necessário
    private Long id;

    private Integer days; // Número de dias de atraso (1, 3, 5, etc.)

    private Double percentage; // Porcentagem de multa a ser aplicada sobre o valor da diária

    @ManyToOne
    private Category category;

}
