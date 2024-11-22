package com.br.luggycar.api.repositories;

import com.br.luggycar.api.entities.OptionalItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OptionalItemRepository extends JpaRepository<OptionalItem, Long> {
    Optional<OptionalItem> findByName(String name);
}