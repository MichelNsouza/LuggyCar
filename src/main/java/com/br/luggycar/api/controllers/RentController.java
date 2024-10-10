package com.br.luggycar.api.controllers;

import com.br.luggycar.api.entities.Rent;
import com.br.luggycar.api.requests.RentRequest;
import com.br.luggycar.api.services.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rent")
public class RentController {

    @Autowired
    RentService rentService;


    @PostMapping
    public ResponseEntity<Rent> createRent(@RequestBody RentRequest rentRequest) {

        Rent rent = rentService.createRent(rentRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(rent);

    }

    @GetMapping
    public ResponseEntity<List<Rent>> readAllRent() {
        return ResponseEntity.ok(rentService.readAllRent());
    }

}
