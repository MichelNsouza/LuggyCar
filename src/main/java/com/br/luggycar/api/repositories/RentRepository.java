package com.br.luggycar.api.repositories;

import com.br.luggycar.api.entities.Rent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentRepository extends JpaRepository<Rent, Long> {

}
