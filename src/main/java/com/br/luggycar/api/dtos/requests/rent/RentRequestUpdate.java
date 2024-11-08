package com.br.luggycar.api.dtos.requests.rent;

import com.br.luggycar.api.dtos.requests.Optional.OptionalQuantityRequest;
import com.br.luggycar.api.enums.rent.RentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentRequestUpdate {

    private RentStatus status;
    private String user;
    private Double totalValue;
    private int totalDays;
    private Double deposit;
    private Double kmInitial;
    private Double kmFinal;
    private Long clientId;
    private Long vehicleId;
    private List<OptionalQuantityRequest> optionalItems;
    private LocalDate update_at;


}
