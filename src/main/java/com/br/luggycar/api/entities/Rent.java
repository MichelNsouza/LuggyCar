package com.br.luggycar.api.entities;

import com.br.luggycar.api.enums.rent.RentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String user;

    private int totalDays;
    private BigDecimal deposit;
    private BigDecimal dailyRate;

    private BigDecimal kmInitial;
    private BigDecimal kmFinal;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Enumerated(EnumType.STRING)
    private RentStatus status;

    @ManyToMany
    @JoinTable(
            name = "rent_optional_items",
            joinColumns = @JoinColumn(name = "rent_id"),
            inverseJoinColumns = @JoinColumn(name = "optional_item_id")
    )
    private Set<OptionalItem> optionalItems = new HashSet<>();

    private LocalDate create_at;
    private LocalDate update_at;

}
