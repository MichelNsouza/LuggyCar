package com.br.luggycar.api.dtos.requests;

public record DelayPenaltyRequest(
        Integer days,
        Double percentage
) {
}
