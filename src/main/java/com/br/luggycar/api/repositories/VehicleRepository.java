package com.br.luggycar.api.repositories;

import com.br.luggycar.api.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByCategoryId(Long categoryId);
    Optional<Vehicle> findByLicensePlate(String plate);

}