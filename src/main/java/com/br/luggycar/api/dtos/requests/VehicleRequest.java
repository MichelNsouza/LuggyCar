package com.br.luggycar.api.dtos.requests;


import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.enums.vehicle.VehicleAccessorie;
import com.br.luggycar.api.enums.vehicle.VehicleColor;
import com.br.luggycar.api.enums.vehicle.VehicleManufacturer;
import com.br.luggycar.api.enums.vehicle.Vehicletransmission;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.Set;

public record VehicleRequest(

        String name,
        VehicleManufacturer manufacturer,
        String version,
//        Long categoryId,
        String categoryName,
        String urlFipe,
        String plate,
        VehicleColor color,
        Vehicletransmission transmission,
        String currentKm,
        String passangerCapacity,
        String trunkCapacity,
        Set<VehicleAccessorie> accessories,
        double dailyRate

) {
}