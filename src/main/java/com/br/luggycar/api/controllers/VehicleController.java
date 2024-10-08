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
@RequestMapping ("/api/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAll() {
        return ResponseEntity.ok(vehicleService.getAll());
    }

    @PostMapping
    public ResponseEntity<Vehicle> saveVehicle(@RequestBody Vehicle vehicle) {
        Category category = categoryService.findById(vehicle.getCategory().getId());
        vehicle.setCategory(category);
        Vehicle savedVehicle = vehicleService.save(vehicle);
        return ResponseEntity.ok(savedVehicle);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Vehicle> deleteVehicle(@PathVariable Long id){
        vehicleService.deleteVehicleById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Vehicle> vehicle = vehicleService.findVehicleById(id);

        if (vehicle.isEmpty()) {
            throw new ResourceNotFoundException("Veículo não encontrado!");
        }
        return ResponseEntity.ok().body(vehicle.get());

    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> update(@PathVariable Long id, @RequestBody VehicleRequest vehicleRequest) throws ResourceNotFoundException {
        Optional<Vehicle> vehicle = vehicleService.findVehicleById(id);

        if (vehicle.isEmpty()) {
            throw new ResourceNotFoundException("Veículo não encontrado");
        }

        Vehicle vehicleResponse = vehicleService.update(id, vehicleRequest);

        return ResponseEntity.ok().body(vehicleResponse);
    }

}