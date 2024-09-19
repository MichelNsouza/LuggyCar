package com.br.luggycar.api.requests;

import com.br.luggycar.api.models.vehicle.VehicleAccessorie;
import com.br.luggycar.api.models.vehicle.VehicleColor;
import com.br.luggycar.api.models.vehicle.VehicleManufacturer;
import com.br.luggycar.api.models.vehicle.Vehicletransmission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequest {

    private String name;

    @Enumerated(EnumType.STRING)
    private VehicleManufacturer manufacturer;

    private String version;

    private String urlFipe;

    private String plate;

    @Enumerated(EnumType.STRING)  // Armazena os valores do enum como strings no banco de dados
    private VehicleColor color;

    @Enumerated(EnumType.STRING)
    private Vehicletransmission transmission;

    private String currentKm;

    private String passangerCapacity;

    private String trunkCapacity;

    @ElementCollection  // Indica que estamos armazenando uma coleção de elementos
    @Enumerated(EnumType.STRING)  // Armazena os valores do enum como strings no banco de dados
    private Set<VehicleAccessorie> accessories;  // Um veículo pode ter múltiplos acessórios

    private double dailyRate;

    private Date registrationDate;

}