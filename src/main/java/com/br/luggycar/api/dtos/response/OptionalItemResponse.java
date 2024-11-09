package com.br.luggycar.api.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OptionalItemResponse {
    private String name;
    private Double rentalValue;
    private int quantity;
}
