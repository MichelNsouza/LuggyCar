    package com.br.luggycar.api.repositories;

    import com.br.luggycar.api.entities.Rent;
    import com.br.luggycar.api.entities.Vehicle;
    import com.br.luggycar.api.enums.rent.RentStatus;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Modifying;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;

    import java.math.BigDecimal;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.util.List;

    public interface RentRepository extends JpaRepository<Rent, Long> {

        List<Rent> findByVehicleIdAndStatus(Long vehicleId, RentStatus status);

        @Query("SELECT COUNT(r) > 0 FROM Rent r WHERE r.client.id = :clientId AND r.status IN :statuses")
        boolean existsActiveRentByClientId(@Param("clientId") Long clientId, @Param("statuses") List<RentStatus> statuses);

        boolean existsByVehicleIdAndStatusIn(Long vehicleId, List<RentStatus> statuses);

        @Query("SELECT r.vehicle FROM Rent r WHERE r.status IN :statuses")
        List<Vehicle> findRentedVehiclesByStatusIn(@Param("statuses") List<RentStatus> statuses);

        @Query("SELECT COUNT(r) > 0 FROM Rent r WHERE r.vehicle.id = :vehicleId AND r.status IN :activeStatuses")
        boolean isVehicleAvailable(@Param("vehicleId") Long vehicleId, @Param("activeStatuses") List<RentStatus> activeStatuses);

        List<Rent> findByVehicleIdAndStatusIn(Long vehicleId, List<RentStatus> statuses);

        @Modifying
        @Query(value = "INSERT INTO rent (client_id, create_at, daily_rate, deposit, km_final, km_initial, status, total_days, update_at, user, vehicle_id) " +
                "VALUES (:clientId, :createAt, :dailyRate, :deposit, :kmFinal, :kmInitial, :status, :totalDays, :updateAt, :user, :vehicleId)", nativeQuery = true)
        void saveRentManually(@Param("clientId") Long clientId,
                              @Param("createAt") LocalDateTime createAt,
                              @Param("dailyRate") BigDecimal dailyRate,
                              @Param("deposit") BigDecimal deposit,
                              @Param("kmFinal") BigDecimal kmFinal,
                              @Param("kmInitial") BigDecimal kmInitial,
                              @Param("status") String status,
                              @Param("totalDays") int totalDays,
                              @Param("updateAt") LocalDateTime updateAt,
                              @Param("user") String user,
                              @Param("vehicleId") Long vehicleId);

    }

