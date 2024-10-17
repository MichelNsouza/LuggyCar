package com.br.luggycar.api.controllers;

import com.br.luggycar.api.dtos.response.RentResponse;
import com.br.luggycar.api.dtos.response.VehicleResponse;
import com.br.luggycar.api.entities.Rent;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.dtos.requests.RentRequest;
import com.br.luggycar.api.services.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rent")
public class RentController {

    @Autowired
    RentService rentService;

    @PostMapping
    public ResponseEntity<RentResponse> createRent(@RequestBody RentRequest rentRequest) {

        RentResponse rentResponse = rentService.createRent(rentRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(rentResponse);

    }

    @GetMapping
    public ResponseEntity<List<RentResponse>> readAllRent() {
        List<RentResponse> rentResponses = rentService.readAllRent();
        return ResponseEntity.ok(rentResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rent> updateClient(@PathVariable Long id, @RequestBody RentRequest rentRequest) throws ResourceNotFoundException {
        Optional<Rent> rent = rentService.findRentById(id);

        if (rent.isEmpty()) {
            throw new ResourceNotFoundException("Locação não encontrada!");
        }

        Rent rentResponse = rentService.updateRent(id, rentRequest);

        return ResponseEntity.ok().body(rentResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Rent> deleteClient(@PathVariable Long id) {
        rentService.deleteRent(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rent> findRentById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Rent> rent = rentService.findRentById(id);

        if (rent.isEmpty()) {
            throw new ResourceNotFoundException("Locação não encontrada!");
        }

        return ResponseEntity.ok().body(rent.get());
    }



}
