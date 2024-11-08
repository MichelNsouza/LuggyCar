package com.br.luggycar.api.repositories;

import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.enums.rent.RentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByCategoryId(Long categoryId);

    Optional<Vehicle> findByPlate(String plate);

    @Query("SELECT v FROM Vehicle v WHERE v NOT IN (SELECT r.vehicle FROM Rent r WHERE r.status = :activeStatus)")
    List<Vehicle> findAvailableVehicles(@Param("activeStatus") RentStatus activeStatus);

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END " +
            "FROM Vehicle v " +
            "WHERE v.id = :vehicleId AND v NOT IN " +
            "(SELECT r.vehicle FROM Rent r WHERE r.status IN :activeStatuses)")
    boolean isVehicleAvailable(@Param("vehicleId") Long vehicleId,
                               @Param("activeStatuses") List<RentStatus> activeStatuses);


}