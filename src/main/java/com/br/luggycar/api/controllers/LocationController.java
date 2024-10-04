package com.br.luggycar.api.controllers;

import com.br.luggycar.api.entities.Location;
import com.br.luggycar.api.requests.LocationRequest;
import com.br.luggycar.api.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/registration")
    public ResponseEntity<Location> saveLocation(@RequestBody LocationRequest locationRequest) {
        Location location = locationService.save(locationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(location);

    }

}
