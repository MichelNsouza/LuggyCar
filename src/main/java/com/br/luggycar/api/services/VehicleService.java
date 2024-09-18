package com.br.luggycar.api.services;

import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    public List <Vehicle> getAll() {
        return vehicleRepository.findAll();
    }
}