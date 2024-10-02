package com.br.luggycar.api.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dailyRate;
    private String totalDays;
    private String deposit;
    private String kmInitial;
    private String kmFinal;
    private LocalDate registration;


    private User user;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

}