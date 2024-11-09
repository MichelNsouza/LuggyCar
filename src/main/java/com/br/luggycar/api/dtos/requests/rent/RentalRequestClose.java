package com.br.luggycar.api.dtos.requests.rent;

import com.br.luggycar.api.entities.*;
import com.br.luggycar.api.entities.rent.RestrictionRental;

import java.time.LocalDate;
import java.util.List;

public record RentalRequestClose(
        LocalDate finishedDate,
        Double kmFinal,
        Accident accident,
        List<RestrictionRental> restrictions
) {
}
