package com.br.luggycar.api.dtos.requests.rent;

import com.br.luggycar.api.entities.Accident;

import java.math.BigDecimal;

public record CloseRentalRequest(
        Long id,
        Double kmFinal,
        Accident accident
) {
}
