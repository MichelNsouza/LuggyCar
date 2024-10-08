package com.br.luggycar.api.requests;

import com.br.luggycar.api.enums.vehicle.VehicleAccessorie;
import com.br.luggycar.api.enums.vehicle.VehicleColor;
import com.br.luggycar.api.enums.vehicle.VehicleManufacturer;
import com.br.luggycar.api.enums.vehicle.Vehicletransmission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequest {
    private String name;
    private VehicleManufacturer manufacturer;
    private String version;
    private String urlFipe;
    private String plate;
    private VehicleColor color;
    private Vehicletransmission transmission;
    private String currentKm;
    private String passangerCapacity;
    private String trunkCapacity;
    private double dailyRate;
    private Date registrationDate;
}