package com.br.luggycar.api.dtos.response.rent;

import com.br.luggycar.api.dtos.response.OptionalItemResponse;
import com.br.luggycar.api.entities.rent.Rent;
import com.br.luggycar.api.enums.rent.RentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class RentResponseUpdate {

    private Long id;
    private RentStatus status;

    private String user;
    private Long clientId;
    private Long vehicleId;

    private LocalDate startDate;
    private int totalDays;
    private LocalDate expectedCompletionDate;
    private LocalDate finishedDate;

    private Double deposit;
    private Double dailyRate;

    private Double totalValueOptionalItems;
    private Double totalValue;

    private Double kmInitial;
    private Double kmFinal;

    private List<OptionalItemResponse> optionalItems;

    private LocalDate update_at;

    public RentResponseUpdate(Rent rent) {
        this.id = rent.getId();
        this.status = rent.getStatus();
        this.user = rent.getUser();
        this.clientId = rent.getClient().getId();
        this.vehicleId = rent.getVehicle().getId();
        this.startDate = rent.getStartDate();
        this.finishedDate = rent.getFinishedDate();
        this.dailyRate = rent.getDailyRate();
        this.totalDays = rent.getTotalDays();
        this.expectedCompletionDate = rent.getExpectedCompletionDate();
        this.deposit = rent.getSecurityDeposit();
        this.kmInitial = rent.getKmInitial();
        this.kmFinal = rent.getKmFinal();
        this.totalValue = rent.getTotalValue();
        this.totalValueOptionalItems = rent.getTotalValueOptionalItems();

        this.update_at = rent.getUpdate_at();


        this.optionalItems = rent.getRentOptionalItems().stream()
                .map(item -> new OptionalItemResponse(
                        item.getOptionalItem().getName(),
                        item.getOptionalItem().getRentalValue(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());
    }


}