package com.br.luggycar.api.dtos.response.rent;

import com.br.luggycar.api.dtos.response.OptionalItemResponse;
import com.br.luggycar.api.entities.rent.Rent;
import com.br.luggycar.api.enums.rent.RentStatus;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class RentResponse {

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

    @Nullable
    private List<OptionalItemResponse> optionalItems;

    private Double dailyRate;
    private Double totalValueOptionalItems;
    private Double totalValue;

    private Double kmInitial;
    private Double kmFinal;

    private LocalDate create_at;
    private LocalDate update_at;

    public RentResponse(Rent rent) {
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
        this.create_at = rent.getCreate_at();
        this.update_at = rent.getUpdate_at();
        this.optionalItems = Optional.ofNullable(rent.getRentOptionalItems())
                .map(items -> items.stream()
                        .map(item -> new OptionalItemResponse(
                                item.getOptionalItem().getName(),
                                item.getOptionalItem().getRentalValue(),
                                item.getQuantity()
                        ))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        this.totalValueOptionalItems = rent.getTotalValueOptionalItems();
    }


}