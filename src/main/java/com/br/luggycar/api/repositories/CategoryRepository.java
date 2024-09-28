package com.br.luggycar.api.repositories;

import com.br.luggycar.api.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    ThreadLocal<Object> findByName(String name);
}
