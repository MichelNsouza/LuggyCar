package com.br.luggycar.api.repositories;

import com.br.luggycar.api.entities.Rent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {
    List<Rent> findByVehicleIdAndActive(Long vehicleId, boolean active);
}
