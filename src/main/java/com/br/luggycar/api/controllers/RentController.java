package com.br.luggycar.api.controllers;

import com.br.luggycar.api.dtos.requests.rent.CloseRentalRequest;
import com.br.luggycar.api.dtos.requests.rent.RentRequestUpdate;
import com.br.luggycar.api.dtos.response.rent.CloseRentalResponse;
import com.br.luggycar.api.dtos.response.rent.RentResponse;
import com.br.luggycar.api.entities.Rent;
import com.br.luggycar.api.exceptions.ResourceBadRequestException;
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
    public ResponseEntity<RentResponse> createRent(@RequestBody RentRequest rentRequest) throws ResourceBadRequestException {
        if (rentService.isVehicleAvailable(rentRequest.vehicleId())) {
            return ResponseEntity.status(HttpStatus.CREATED).body(rentService.createRent(rentRequest));
        } else {
            throw new ResourceNotFoundException("Deu uruim");
        }

    }

    @GetMapping
    public ResponseEntity<List<RentResponse>> readAllRent() throws ResourceNotFoundException{
        return ResponseEntity.ok(rentService.readAllRent());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RentResponse> updateClient(@PathVariable Long id, @RequestBody RentRequestUpdate rentRequest) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(rentService.updateRent(id, rentRequest));
    }

    @PatchMapping("/status")
    public ResponseEntity<CloseRentalResponse> updateRentStatus(@RequestBody CloseRentalRequest closeRentalRequest) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(rentService.closeRental(closeRentalRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Rent> deleteClient(@PathVariable Long id) throws ResourceNotFoundException {
        rentService.deleteRent(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<RentResponse>> findRentById(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(rentService.findRentById(id));
    }

    @PutMapping("/close")
    public ResponseEntity<CloseRentalResponse> closeRental(@RequestBody CloseRentalRequest closeRentalRequest) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(rentService.closeRental(closeRentalRequest));
    }

}
