package com.br.luggycar.api.dtos.response.rent;

import com.br.luggycar.api.dtos.response.OptionalItemResponse;
import com.br.luggycar.api.entities.rent.Rent;
import com.br.luggycar.api.enums.rent.RentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
public class CloseRentalResponse {
    private Long id;
    private RentStatus status;

    private String user;
    private Long clientId;
    private Long vehicleId;

    private Double deposit;

    private LocalDate startDate;
    private int totalDays;
    private LocalDate expectedCompletionDate;
    private LocalDate finishedDate;

    private Double dailyRate;
    private Double totalValueOptionalItems;
    private Double totalDailyPenalty = 0.0;
    private Double totalValue;

    private List<OptionalItemResponse> optionalItems;

    private Double kmInitial;
    private Double kmFinal;

    private LocalDate create_at;
    private LocalDate update_at;

    public CloseRentalResponse(Rent rent, Double totalDailyPenalty) {
        this.id = rent.getId();
        this.status = rent.getStatus();
        this.user = rent.getUser();
        this.clientId = rent.getClient().getId();
        this.vehicleId = rent.getVehicle().getId();
        this.startDate = rent.getStartDate();
        this.dailyRate = rent.getDailyRate();
        this.totalDays = rent.getTotalDays();
        this.expectedCompletionDate = rent.getExpectedCompletionDate();
        this.finishedDate = rent.getFinishedDate();
        this.deposit = rent.getSecurityDeposit();
        this.kmInitial = rent.getKmInitial();
        this.kmFinal = rent.getKmFinal();
        this.totalValue = rent.getTotalValue();
        this.totalDailyPenalty = totalDailyPenalty;
        this.optionalItems = rent.getRentOptionalItems().stream()
                .map(item -> new OptionalItemResponse(
                        item.getOptionalItem().getName(),
                        item.getOptionalItem().getRentalValue(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());
        this.totalValueOptionalItems = rent.getTotalValueOptionalItems();
        this.create_at = rent.getCreate_at();
        this.update_at = rent.getUpdate_at();
    }



}