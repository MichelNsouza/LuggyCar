package com.br.luggycar.api.repositories;

import com.br.luggycar.api.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByCategoryId(Long categoryId);
}