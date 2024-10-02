package com.br.luggycar.api.controllers;

import com.br.luggycar.api.entities.Location;
import com.br.luggycar.api.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    @Autowired
    LocationService locationService;

    @GetMapping
    public ResponseEntity<List<Location>> getAll() {
        return ResponseEntity.ok(locationService.getAll());
    }

}
