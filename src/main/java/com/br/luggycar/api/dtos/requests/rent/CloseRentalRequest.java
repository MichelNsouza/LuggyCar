package com.br.luggycar.api.dtos.requests.rent;

import java.math.BigDecimal;

public record CloseRentalRequest(
        Long id,
        BigDecimal kmFinal
) {
}
