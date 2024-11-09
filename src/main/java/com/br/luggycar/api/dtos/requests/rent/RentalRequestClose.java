package com.br.luggycar.api.dtos.requests.rent;

import com.br.luggycar.api.entities.*;
import com.br.luggycar.api.enums.rent.RentStatus;

import java.time.LocalDate;
import java.util.List;

public record RentalRequestClose(
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
        List<OptionalItem> optionalItems,
        List<RentOptionalItem> rentOptionalItems,
        LocalDate create_at,
        LocalDate update_at
) {
}
