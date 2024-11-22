package com.br.luggycar.api.repositories;

import com.br.luggycar.api.entities.rent.Rent;
import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.enums.rent.RentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {

    List<Rent> findByClientId(Long clientId);

    @Query("SELECT COUNT(r) > 0 FROM Rent r WHERE r.client.id = :clientId AND r.status IN :statuses")
    boolean existsActiveRentByClientId(@Param("clientId") Long clientId, @Param("statuses") List<RentStatus> statuses);

    boolean existsByVehicleIdAndStatusIn(Long vehicleId, List<RentStatus> statuses);

    @Query("SELECT r.vehicle FROM Rent r WHERE r.status IN :statuses")
    List<Vehicle> findRentedVehiclesByStatusIn(@Param("statuses") List<RentStatus> statuses);

    @Query("SELECT r.vehicle FROM Rent r WHERE r.status IN :statuses AND r.vehicle.statusVehicle = com.br.luggycar.api.enums.vehicle.StatusVehicle.AVAILABLE")
    List<Vehicle> findRentedVehiclesByStatusInAndStatusVehicleAvailable(@Param("statuses") List<RentStatus> statuses);


}

