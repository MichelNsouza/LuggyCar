package com.br.luggycar.api.dtos.response;

import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.enums.vehicle.VehicleAccessorie;
import com.br.luggycar.api.enums.vehicle.VehicleColor;
import com.br.luggycar.api.enums.vehicle.VehicleManufacturer;
import com.br.luggycar.api.enums.vehicle.Vehicletransmission;

import java.util.Set;

public record VehicleResponse(
        String name,
        VehicleManufacturer manufacturer,
        String version,
        Long categoryId,
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
    public VehicleResponse(Vehicle vehicle) {
        this(
                vehicle.getName(),
                vehicle.getManufacturer(),
                vehicle.getVersion(),
                vehicle.getCategory().getId(),
                vehicle.getUrlFipe(),
                vehicle.getPlate(),
                vehicle.getColor(),
                vehicle.getTransmission(),
                vehicle.getCurrentKm(),
                vehicle.getPassangerCapacity(),
                vehicle.getTrunkCapacity(),
                vehicle.getAccessories(),
                vehicle.getDailyRate()
        );
    }
}
