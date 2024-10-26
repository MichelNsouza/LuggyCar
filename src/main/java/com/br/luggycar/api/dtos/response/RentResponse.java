package com.br.luggycar.api.dtos.response;

import com.br.luggycar.api.entities.Rent;
import com.br.luggycar.api.enums.rent.RentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RentResponse(
        Long id,
        RentStatus status,
        BigDecimal dailyRate,
        int totalDays,
        BigDecimal deposit,
        BigDecimal kmInitial,
        BigDecimal kmFinal,
        LocalDate registration,
        String user,
        Long clientId,
        Long vehicleId
) {
    public RentResponse(Rent rent) {
        this(
                rent.getId(),
                rent.getStatus(),
                rent.getDailyRate(),
                rent.getTotalDays(),
                rent.getDeposit(),
                rent.getKmInitial(),
                rent.getKmFinal(),
                rent.getRegistration(),
                rent.getUser(),
                rent.getClient() != null ? rent.getClient().getId() : null,
                rent.getVehicle() != null ? rent.getVehicle().getId() : null
        );
    }
}
