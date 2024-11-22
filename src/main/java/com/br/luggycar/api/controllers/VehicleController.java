package com.br.luggycar.api.controllers;

import com.br.luggycar.api.dtos.response.VehicleResponse;
import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.exceptions.ResourceDatabaseException;
import com.br.luggycar.api.exceptions.ResourceExistsException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.dtos.requests.VehicleRequest;
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
    public ResponseEntity<VehicleResponse> createVehicle(@RequestBody VehicleRequest vehicleRequest) throws ResourceExistsException {

        VehicleResponse savedVehicle = vehicleService.createVehicle(vehicleRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);

    }

    @GetMapping
    public ResponseEntity<List<VehicleResponse>> readAllVehicles() {
        List<VehicleResponse> vehicleResponses = vehicleService.readAllVehicle();
        return ResponseEntity.ok(vehicleResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponse> updateVehicle(@PathVariable Long id, @RequestBody VehicleRequest vehicleRequest)
            throws ResourceNotFoundException, ResourceExistsException {

        VehicleResponse updatedVehicleResponse = vehicleService.updateVehicle(id, vehicleRequest);
        return ResponseEntity.ok(updatedVehicleResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Vehicle> deleteVehicle(@PathVariable Long id) throws ResourceExistsException {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> findVehicleById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<VehicleResponse> vehicleResponse = vehicleService.findVehicleById(id);

        if (vehicleResponse.isEmpty()) {
            throw new ResourceNotFoundException("Veículo não encontrado!");
        }
        return ResponseEntity.ok().body(vehicleResponse.get());

    }

    @GetMapping("/plate/{plate}")
    public ResponseEntity<VehicleResponse> getVehicleByLicensePlate(@PathVariable String plate) {
        VehicleResponse response = vehicleService.getByPlate(plate);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/available")
    public ResponseEntity<List<VehicleResponse>> findAllVehicleAvailable() throws ResourceNotFoundException, ResourceDatabaseException {

        List<VehicleResponse> vehicleResponses = vehicleService.getAvailableVehicles();
        return ResponseEntity.ok(vehicleResponses);

    }
}