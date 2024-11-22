package com.br.luggycar.api.dtos.requests.rent;

import com.br.luggycar.api.dtos.requests.Optional.OptionalQuantityRequest;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record RentRequestCreate(
        Long client_id,
        Long vehicle_id,
        int totalDays,
        Double securityDeposit,
        @Nullable
        List<OptionalQuantityRequest> optionalItems,
        LocalDate startDate,
        LocalDate create_at
) {

    public RentRequestCreate {
        create_at = LocalDate.now();
    }

}
