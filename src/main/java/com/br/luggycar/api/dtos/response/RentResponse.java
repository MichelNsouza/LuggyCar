package com.br.luggycar.api.dtos.response;

import com.br.luggycar.api.entities.OptionalItem;
import com.br.luggycar.api.entities.Rent;
import com.br.luggycar.api.enums.rent.RentStatus;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
public class RentResponse {

    private Long id;
    private RentStatus status;

    private String user;
    private Long clientId;
    private Long vehicleId;

    private BigDecimal deposit;
    private int totalDays;
    private BigDecimal dailyRate;
    private List<OptionalItem> optionalItems;

    private BigDecimal kmInitial;
    private BigDecimal kmFinal;

    private LocalDate create_at;
    private LocalDate update_at;

    // Construtor que aceita um objeto Rent
    public RentResponse(Rent rent) {
        this.id = rent.getId();
        this.status = rent.getStatus();
        this.user = rent.getUser();
        this.clientId = rent.getClient() != null ? rent.getClient().getId() : null;
        this.vehicleId = rent.getVehicle() != null ? rent.getVehicle().getId() : null;
        this.dailyRate = rent.getDailyRate();
        this.totalDays = rent.getTotalDays();
        this.deposit = rent.getDeposit();
        this.kmInitial = rent.getKmInitial();
        this.kmFinal = rent.getKmFinal();
        this.create_at = rent.getCreate_at();
        this.update_at = rent.getUpdate_at();
    }

}
