package com.br.luggycar.api.dtos.requests;

import com.br.luggycar.api.enums.accident.Severity;

import java.util.Date;

public record AccidentRequest(
        Severity severity,
        String description,
        Date registrationDate
) {
}
