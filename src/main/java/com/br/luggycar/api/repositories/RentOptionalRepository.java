package com.br.luggycar.api.repositories;

import com.br.luggycar.api.entities.rent.RentOptionalItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentOptionalRepository extends JpaRepository<RentOptionalItem, Long> {
}
