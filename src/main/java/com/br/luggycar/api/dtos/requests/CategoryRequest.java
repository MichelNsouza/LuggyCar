package com.br.luggycar.api.dtos.requests;

import java.util.List;

public record CategoryRequest(
        String name,
        String description,
        List<DelayPenaltyRequest> delayPenalties // Alterado para DTO específico
) {
}
