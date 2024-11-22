package com.br.luggycar.api.dtos.response.rent;

import com.br.luggycar.api.entities.rent.Rent;
import com.br.luggycar.api.enums.rent.RentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CloseRentalResponse {

    private Long id;
    private RentStatus status;
    private Long clientId;
    private Long vehicleId;
    private Double securityDeposit;
    private int totalDays;
    private LocalDate startDate;
    private LocalDate expectedCompletionDate;
    private LocalDate finishedDate;
    private Double dailyRate;
    private Double totalValueOptionalItems;
    private Double totalPenalty = 0.0;
    private Double totalValue;

    public CloseRentalResponse(Rent rent, Double totalDailyPenalty) {
        this.id = rent.getId();
        this.status = rent.getStatus();
        this.clientId = rent.getClient().getId();
        this.vehicleId = rent.getVehicle().getId();
        this.startDate = rent.getStartDate();
        this.dailyRate = rent.getDailyRate();
        this.totalDays = rent.getTotalDays();
        this.expectedCompletionDate = rent.getExpectedCompletionDate();
        this.finishedDate = rent.getFinishedDate();
        this.securityDeposit = rent.getSecurityDeposit();
        this.totalValue = rent.getTotalValue();
        this.totalPenalty = totalDailyPenalty;
        this.totalValueOptionalItems = rent.getTotalValueOptionalItems();
    }
}