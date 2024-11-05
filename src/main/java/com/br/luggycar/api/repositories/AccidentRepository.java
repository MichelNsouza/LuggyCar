package com.br.luggycar.api.repositories;

import com.br.luggycar.api.entities.Accident;
import com.br.luggycar.api.entities.OptionalItem;
import com.br.luggycar.api.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccidentRepository extends JpaRepository<Accident, Long> {
    Optional<Accident>findByDescription(String description);

}