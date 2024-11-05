package com.br.luggycar.api.controllers;

import com.br.luggycar.api.dtos.requests.rent.CloseRentalRequest;
import com.br.luggycar.api.dtos.response.CloseRentalResponse;
import com.br.luggycar.api.dtos.response.RentResponse;
import com.br.luggycar.api.entities.Rent;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.dtos.requests.rent.RentRequest;
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
    public ResponseEntity<RentResponse> updateClient(@PathVariable Long id, @RequestBody RentRequest rentRequest) throws ResourceNotFoundException {
         Optional<RentResponse> rentOpt = rentService.findRentById(id);

        if (rentOpt.isEmpty()) {
            throw new ResourceNotFoundException("Locação não encontrada!");
        }

        RentResponse rentResponse = rentService.updateRent(id, rentRequest);

        return ResponseEntity.ok().body(rentResponse);

    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<CloseRentalResponse> updateRentStatus( @PathVariable Long id, @RequestBody CloseRentalRequest closeRentalRequest) {
        CloseRentalResponse closeRentalResponse = rentService.closeRental(closeRentalRequest);
        return ResponseEntity.ok().body(closeRentalResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Rent> deleteClient(@PathVariable Long id) {
        rentService.deleteRent(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentResponse> findRentById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<RentResponse> rent = rentService.findRentById(id);

        if (rent.isEmpty()) {
            throw new ResourceNotFoundException("Locação não encontrada!");
        }

        return ResponseEntity.ok().body(rent.get());
    }



}
