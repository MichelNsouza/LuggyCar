package com.br.luggycar.api.entities.rent;

import com.br.luggycar.api.enums.rent.RestrictionType;
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
    private Rent rent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestrictionType restrictionType;

    private String description;
}
