package com.br.luggycar.api.controllers;

import com.br.luggycar.api.entities.Accident;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.dtos.requests.AccidentRequest;
import com.br.luggycar.api.services.AccidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accident")
public class AccidentController {

    @Autowired
    private AccidentService accidentService;

    @PostMapping
    public ResponseEntity<Accident> createAccident(@RequestBody AccidentRequest accidentRequest) throws ResourceNotFoundException {
        return ResponseEntity.ok(accidentService.createAccident(accidentRequest));
    }

    @GetMapping
    public ResponseEntity<List<Accident>> readAllAccidents() {
        return ResponseEntity.ok(accidentService.readAllAccident());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Accident> updateAccident(@PathVariable Long id, @RequestBody AccidentRequest accidentRequest) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(accidentService.updateAccident(id, accidentRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Accident> deleteAccident(@PathVariable Long id) {
        accidentService.deleteAccident(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


    @GetMapping("/{id}")
    public ResponseEntity<Accident> findAccidentById(@PathVariable Long id) {
        return ResponseEntity.ok(accidentService.findAccidentById(id));
    }

}
