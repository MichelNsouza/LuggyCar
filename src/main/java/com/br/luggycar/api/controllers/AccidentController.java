package com.br.luggycar.api.controllers;

import com.br.luggycar.api.entities.Accident;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.requests.AccidentRequest;
import com.br.luggycar.api.services.AccidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accident")
public class AccidentController {

    @Autowired
    private AccidentService accidentService;

    @PostMapping
    public ResponseEntity<Accident> createAccident(@RequestBody AccidentRequest accidentRequest) {
        Accident newAccident = accidentService.createAccident(accidentRequest);
        return ResponseEntity.ok(newAccident);
    }

    @GetMapping
    public List<Accident> readAllAccidents() {
        return accidentService.readAllAccident();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Accident> updateAccident(@PathVariable Long id, @RequestBody AccidentRequest accidentRequest) throws ResourceNotFoundException {

        Optional<Accident> accident = accidentService.findAccidentById(id);

        if (accident.isEmpty()) {
            throw new ResourceNotFoundException("Sinistro não encontrado!");
        }

        Accident accidentResponse = accidentService.updateAccident(id, accidentRequest);

        return ResponseEntity.ok().body(accidentResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Accident> deleteAccident(@PathVariable Long id){
        accidentService.deleteAccident(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Accident> getAccidentById(@PathVariable Long id) {
        return ResponseEntity.ok(accidentService.findAccidentById(id));
    }

}
