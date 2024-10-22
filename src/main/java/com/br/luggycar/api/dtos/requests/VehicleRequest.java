package com.br.luggycar.api.dtos.requests;


import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.enums.vehicle.VehicleColor;
import com.br.luggycar.api.enums.vehicle.VehicleManufacturer;
import com.br.luggycar.api.enums.vehicle.Vehicletransmission;

public record VehicleRequest(

        String name,
        VehicleManufacturer manufacturer,
        String version,
        Category category,
        String urlFipe,
        String plate,
        VehicleColor color,
        Vehicletransmission transmission,
        String currentKm,
        String passangerCapacity,
        String trunkCapacity,
        double dailyRate

) {
}