package com.br.luggycar.api.entities;

import com.br.luggycar.api.enums.rent.RentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private RentStatus status;
    private String user;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false ,unique = false)
    private Vehicle vehicle;
    private int totalDays;
    private Double securityDeposit;
    private LocalDate startDate;
    private LocalDate expectedCompletionDate;
    private LocalDate finishedDate;
    private Double dailyRate;
    private Double totalValue;
    private Double totalValueOptionalItems;
    private Double kmInitial;
    private Double kmFinal;


    @OneToMany(mappedBy = "rent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RentOptionalItem> rentOptionalItems;


    private LocalDate create_at;
    private LocalDate update_at;
}

