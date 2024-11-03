package com.br.luggycar.api.dtos.requests;

import com.br.luggycar.api.enums.rent.RentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RentRequest(

        RentStatus status,

        BigDecimal dailyRate,
        int totalDays,
        BigDecimal deposit,

        BigDecimal kmInitial,
        BigDecimal kmFinal,


        Long userId,
        Long clientId,
        Long vehicleId,
        List<Long> optionalItemIds,


        LocalDate create_at,
        LocalDate update_at
) {
    public RentRequest {
        status = RentStatus.IN_PROGRESS;
        create_at = LocalDate.now();
    }
}
