package com.br.luggycar.api.entities;

import com.br.luggycar.api.enums.vehicle.VehicleAccessorie;
import com.br.luggycar.api.enums.vehicle.VehicleColor;
import com.br.luggycar.api.enums.vehicle.VehicleManufacturer;
import com.br.luggycar.api.enums.vehicle.Vehicletransmission;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private VehicleManufacturer manufacturer;

    private String version;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private Category category;

    private String urlFipe;

    private String plate;

    @Enumerated(EnumType.STRING)
    private VehicleColor color;

    @Enumerated(EnumType.STRING)
    private Vehicletransmission transmission;

    private String currentKm;

    private String passangerCapacity;

    private String trunkCapacity;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<VehicleAccessorie> accessories;

    private double dailyRate;

    private LocalDate registrationDate;
}
