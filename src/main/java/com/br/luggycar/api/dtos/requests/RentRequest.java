package com.br.luggycar.api.dtos.requests;


import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.entities.User;
import com.br.luggycar.api.entities.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RentRequest(
     BigDecimal dailyRate,
     int totalDays,
     BigDecimal deposit,
     BigDecimal kmInitial,
     BigDecimal kmFinal,
     LocalDate registration,
     User user,
     Client client,
     Vehicle vehicle
){}
