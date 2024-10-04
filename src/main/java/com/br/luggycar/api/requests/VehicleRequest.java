package com.br.luggycar.api.requests;

import com.br.luggycar.api.enums.vehicle.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequest {

    private String name;

    @Enumerated(EnumType.STRING)
    private VehicleManufacturer manufacturer;

    private String version;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String urlFipe;

    private String plate;

    @Enumerated(EnumType.STRING)
    private VehicleColor color;

    @Enumerated(EnumType.STRING)
    private Vehicletransmission transmission;

    private String currentKm;

    private String passangerCapacity;

    private String trunkCapacity;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<VehicleAccessorie> accessories;

    private double dailyRate;

    private LocalDate registrationDate;

}