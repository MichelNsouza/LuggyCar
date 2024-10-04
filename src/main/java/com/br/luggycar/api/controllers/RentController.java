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

    @GetMapping
    public ResponseEntity<List<Rent>> getAll() {
        return ResponseEntity.ok(rentService.getAll());
    }

    @PostMapping("/registration")
    public ResponseEntity<Rent> saveLocation(@RequestBody RentRequest rentRequest) {
        Rent rent = rentService.save(rentRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(rent);

    }

}
