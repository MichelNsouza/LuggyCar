package com.br.luggycar.api.dtos.requests;


import com.br.luggycar.api.entities.DelayPenalty;

import java.time.LocalDate;
import java.util.List;

public record CategoryRequest(
        String name,
        String description,
        List<DelayPenalty>delayPenalties
) {
}