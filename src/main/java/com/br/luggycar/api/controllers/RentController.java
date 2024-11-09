package com.br.luggycar.api.controllers;

import com.br.luggycar.api.dtos.requests.rent.RentalRequestClose;
import com.br.luggycar.api.dtos.requests.rent.RentRequestUpdate;
import com.br.luggycar.api.dtos.response.rent.CloseRentalResponse;
import com.br.luggycar.api.dtos.response.rent.RentResponse;
import com.br.luggycar.api.entities.Rent;
import com.br.luggycar.api.exceptions.ResourceBadRequestException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.dtos.requests.rent.RentRequestCreate;
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
    public ResponseEntity<RentResponse> createRent(@RequestBody RentRequestCreate rentRequestCreate) throws ResourceBadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(rentService.createRent(rentRequestCreate));
    }

//    @GetMapping
//    public ResponseEntity<List<RentResponse>> readAllRent(){
//        return ResponseEntity.ok(rentService.readAllRent());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<RentResponse> updateClient(@PathVariable Long id, @RequestBody RentRequestUpdate rentRequest) throws ResourceBadRequestException {
//        return ResponseEntity.ok().body(rentService.updateRent(id, rentRequest));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Rent> deleteClient(@PathVariable Long id) throws ResourceNotFoundException {
//        rentService.deleteRent(id);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Optional<RentResponse>> findRentById(@PathVariable Long id) throws ResourceNotFoundException {
//        return ResponseEntity.ok().body(rentService.findRentById(id));
//    }
//
//    @PutMapping("/close")
//    public ResponseEntity<CloseRentalResponse> closeRental(@RequestBody RentalRequestClose rentalRequestClose) throws ResourceBadRequestException {
//        return ResponseEntity.ok().body(rentService.closeRental(rentalRequestClose));
//    }
//
//    @GetMapping("/client/{id}")
//    public  ResponseEntity<List<RentResponse>> findAllRentByClientId(@PathVariable Long id) throws ResourceNotFoundException {
//        return ResponseEntity.ok().body(rentService.findAllRentByClientId(id));
//    }

}
