package com.br.luggycar.api.controllers;

import com.br.luggycar.api.entities.Accident;
import com.br.luggycar.api.services.AccidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accidents")
public class AccidentController {

    @Autowired
    private AccidentService accidentService;


    @GetMapping
    public List<Accident> getAllAccidents() {
        return accidentService.getAllAccidents();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Accident> getAccidentById(@PathVariable Long id) {
        return ResponseEntity.ok(accidentService.findAccidentById(id));
    }

    @PostMapping
    public ResponseEntity<Accident> createAccident(@RequestBody AccidentRequest accidentRequest) {
        Accident newAccident = accidentService.insertAccident(accidentRequest);
        return ResponseEntity.ok(newAccident);
    }
}
