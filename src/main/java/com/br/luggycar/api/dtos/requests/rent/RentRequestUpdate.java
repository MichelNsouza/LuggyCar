package com.br.luggycar.api.dtos.requests.rent;

import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.entities.OptionalItem;
import com.br.luggycar.api.entities.rent.RentOptionalItem;
import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.enums.rent.RentStatus;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;


public record RentRequestUpdate(
        RentStatus status,
        String user,
        Client client,
        Vehicle vehicle,
        int totalDays,
        Double securityDeposit,
        LocalDate rentalDateTimeIn,
        LocalDate rentalDateTimeOut,
        Double dailyRate,
        Double kmInitial,
        Double kmFinal,
        @Nullable
        List<OptionalItem> optionalItems,
        List<RentOptionalItem> rentOptionalItems,
        LocalDate create_at,
        LocalDate update_at
) {
}
