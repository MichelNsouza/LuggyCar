package com.br.luggycar.api.dtos.requests.rent;

import com.br.luggycar.api.enums.rent.RentStatus;

public record RentStatusRequest(
        RentStatus status
) {
}
