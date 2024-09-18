package com.br.luggycar.api.requests;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequest {

    private String name;
    private String manufacturer;
    private String version;
    private String urlFipe;
    private String plate;
    private String color;
    private String transmission;
    private String currentKm;
    private String passangerCapacity;
    private String trunkCapacity;
    private String accessories;
    private String dailyRate;
    private String registrationDate;

}