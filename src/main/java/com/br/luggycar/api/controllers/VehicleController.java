package com.br.luggycar.api.controllers;
import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping ("/api/vehicle")

public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public ResponseEntity <List<Vehicle>> getAll() {
        return ResponseEntity.ok(vehicleService.getAll());
    }

}