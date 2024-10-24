package com.br.luggycar.api.repositories;

import com.br.luggycar.api.entities.Rent;
import com.br.luggycar.api.enums.rent.RentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {
    List<Rent> findByVehicleIdAndActive(Long vehicleId, boolean active);

    @Query("SELECT COUNT(r) > 0 FROM Rent r WHERE r.client.id = :clientId AND r.status = :status")
    boolean existsActiveRentByClientId(@Param("clientId") Long clientId, @Param("status") RentStatus status);

}
