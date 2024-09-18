package com.br.luggycar.api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String name;
    private String manufacturer;
    private String version;
    private String urlFipe;
    private String plate;
    private String color;
    private String transmission;
    private String currentKm;
    private String passangerCapacity;
    private String trunkCapacity;
    private String accessories;
    private String dailyRate;
    private String registrationDate;

}