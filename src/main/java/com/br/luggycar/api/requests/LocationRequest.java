package com.br.luggycar.api.requests;


import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.entities.User;
import com.br.luggycar.api.entities.Vehicle;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationRequest {

    private String dailyRate;
    private String totalDays;
    private String deposit;
    private String kmInitial;
    private String kmFinal;
    private LocalDate registration;

    private User user;

    private Client client;

    private Vehicle vehicle;


}
