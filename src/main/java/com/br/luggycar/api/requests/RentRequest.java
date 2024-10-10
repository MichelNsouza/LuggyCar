package com.br.luggycar.api.requests;


import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.entities.User;
import com.br.luggycar.api.entities.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public record RentRequest(
     String dailyRate,
     String totalDays,
     String deposit,
     String kmInitial,
     String kmFinal,
     LocalDate registration,
     User user,
     Client client,
     Vehicle vehicle
){}
