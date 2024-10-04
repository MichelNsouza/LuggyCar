package com.br.luggycar.api.repositories;

import com.br.luggycar.api.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {

}
