package com.br.luggycar.api.dtos.requests.rent;

import com.br.luggycar.api.dtos.requests.Optional.OptionalQuantityRequest;
import com.br.luggycar.api.enums.rent.RentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record RentRequest(

        RentStatus status,

        String user,

        int totalDays,
        BigDecimal deposit,
        BigDecimal kmInitial,

        Long clientId,
        Long vehicleId,
        List<OptionalQuantityRequest> optionalItems,

        LocalDate create_at
) {
    public RentRequest {
        status = RentStatus.IN_PROGRESS;
        create_at = LocalDate.now();
    }
}
