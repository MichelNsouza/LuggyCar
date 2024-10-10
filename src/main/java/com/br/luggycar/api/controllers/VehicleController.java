package com.br.luggycar.api.controllers;

import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.requests.VehicleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.services.VehicleService;
import com.br.luggycar.api.services.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {

        Category category = categoryService.findCategoryById(vehicle.getCategory().getId());

        vehicle.setCategory(category);

        Vehicle savedVehicle = vehicleService.createVehicle(vehicle);

        return ResponseEntity.ok(savedVehicle);

    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> readAllVehicles() {
        return ResponseEntity.ok(vehicleService.readAllVehicle());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody VehicleRequest vehicleRequest) throws ResourceNotFoundException {
        Optional<Vehicle> vehicle = vehicleService.findVehicleById(id);

        if (vehicle.isEmpty()) {
            throw new ResourceNotFoundException("Veículo não encontrado");
        }

        Vehicle vehicleResponse = vehicleService.updateVehicle(id, vehicleRequest);

        return ResponseEntity.ok().body(vehicleResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Vehicle> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> findVehicleById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Vehicle> vehicle = vehicleService.findVehicleById(id);

        if (vehicle.isEmpty()) {
            throw new ResourceNotFoundException("Veículo não encontrado!");
        }
        return ResponseEntity.ok().body(vehicle.get());

    }


}