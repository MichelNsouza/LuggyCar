package com.br.luggycar.api.repositories;

import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.enums.rent.RentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM Rent r " +
            "WHERE r.vehicle.category.id = :categoryId AND r.status <> :completedStatus")
    boolean existsByCategoryIdAndStatusNotCompleted(@Param("categoryId") Long categoryId,
                                                    @Param("completedStatus") RentStatus completedStatus);
}
