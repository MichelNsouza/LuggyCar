package com.br.luggycar.api.controllers;

import com.br.luggycar.api.entities.Accident;
import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.enums.accident.Severity;
import com.br.luggycar.api.repositories.AccidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/accident")
public class AccidentController {

    @Autowired
    private AccidentRepository accidentRepository;

    @GetMapping
   public List<Accident> getAllAccidents() {
        return accidentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Accident> getAccidentById(@PathVariable Long id) {
        return ResponseEntity.ok(accidentRepository.findById(id));
    }

}
