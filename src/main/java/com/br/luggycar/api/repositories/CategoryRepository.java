package com.br.luggycar.api.repositories;

import com.br.luggycar.api.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
//    ThreadLocal<Object> findByName(String name);
     Optional<Category> findByName(String name);
}
