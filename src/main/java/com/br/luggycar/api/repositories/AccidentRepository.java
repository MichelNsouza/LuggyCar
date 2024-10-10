package com.br.luggycar.api.repositories;

import com.br.luggycar.api.entities.Accident;
import com.br.luggycar.api.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccidentRepository extends JpaRepository<Accident, Long> {

}