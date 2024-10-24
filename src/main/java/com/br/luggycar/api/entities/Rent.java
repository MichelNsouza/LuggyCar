package com.br.luggycar.api.entities;


import com.br.luggycar.api.enums.rent.RentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal dailyRate;
    private int totalDays;
    private BigDecimal deposit;
    private BigDecimal kmInitial;
    private BigDecimal kmFinal;
    private LocalDate registration;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

    private String user;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    private boolean active;

    @Enumerated(EnumType.STRING)
    private RentStatus status;

}