package com.br.luggycar.api.dtos.requests;


import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.entities.User;
import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.enums.rent.RentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RentRequest(
     BigDecimal dailyRate,
     int totalDays,
     BigDecimal deposit,
     BigDecimal kmInitial,
     BigDecimal kmFinal,
     LocalDate create_at,
     Local update_at,
     RentStatus status,
     User user,
     Client client,
     Vehicle vehicle
){}
