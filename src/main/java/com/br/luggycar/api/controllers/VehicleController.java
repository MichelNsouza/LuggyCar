package com.br.luggycar.api.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.requests.VehicleRequest;
import com.br.luggycar.api.services.VehicleService;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/registration")
    public ResponseEntity<Vehicle> insert(@RequestBody VehicleRequest vehicleRequest) {
    Vehicle vehicle = vehicleService.insert(vehicleRequest);


        return ResponseEntity.status(HttpStatus.CREATED).body(vehicle);
    }

}