package com.br.luggycar.api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DelayPenalty {

    @Id
    private Long id;

    private Integer days; // Número de dias de atraso (1, 3, 5, etc.)

    private Double percentage; // Porcentagem de multa a ser aplicada sobre o valor da diária

    @ManyToOne
    private Category category;

}
