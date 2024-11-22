package com.br.luggycar.api.entities.rent;

import com.br.luggycar.api.enums.rent.RestrictionType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestrictionRental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rent_id")
    @JsonBackReference
    private Rent rent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestrictionType restrictionType;

    private String description;
}
