package com.br.luggycar.api.services;

import com.br.luggycar.api.entities.vehicle.Vehicle;
import com.br.luggycar.api.repositories.VehicleRepository;
import com.br.luggycar.api.requests.VehicleRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    public List <Vehicle> getAll() {
        return vehicleRepository.findAll();
    }

public Vehicle insert (VehicleRequest vehicleRequest){

    Vehicle vehicle = new Vehicle();
    BeanUtils.copyProperties(vehicle, vehicleRequest);

    return vehicleRepository.save(vehicle);



}



}