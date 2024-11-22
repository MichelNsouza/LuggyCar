package com.br.luggycar.api.dtos.response;

import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.enums.vehicle.*;

import java.util.Set;

public record VehicleResponse(
        Long id,
        StatusVehicle statusVehicle,
        String name,
        VehicleManufacturer manufacturer,
        String version,
        String categoryName,
        String urlFipe,
        String plate,
        VehicleColor color,
        Vehicletransmission transmission,
        Double currentKm,
        String passangerCapacity,
        String trunkCapacity,
        Set<VehicleAccessorie> accessories,
        double dailyRate
) {
    public VehicleResponse(Vehicle vehicle) {
        this(
                vehicle.getId(),
                vehicle.getStatusVehicle(),
                vehicle.getName(),
                vehicle.getManufacturer(),
                vehicle.getVersion(),
                vehicle.getCategory().getName(),
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
