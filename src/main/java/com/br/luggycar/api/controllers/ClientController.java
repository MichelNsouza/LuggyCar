package com.br.luggycar.api.controllers;

import com.br.luggycar.api.dtos.requests.ClientRequest;
import com.br.luggycar.api.dtos.response.ClientResponse;
import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.exceptions.ResourceClientHasActiveRentalsException;
import com.br.luggycar.api.exceptions.ResourceDatabaseException;
import com.br.luggycar.api.exceptions.ResourceExistsException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@RequestBody @Valid ClientRequest clientRequest) throws ResourceExistsException, ResourceDatabaseException, ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(clientRequest));
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> readAllClient() throws ResourceDatabaseException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clientService.readAllClient());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Long id, @RequestBody ClientRequest clientRequest) throws ResourceDatabaseException, ResourceNotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clientService.updateClient(id, clientRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Client> deleteClient(@PathVariable Long id) throws ResourceDatabaseException, ResourceClientHasActiveRentalsException, ResourceNotFoundException {
        clientService.deleteClient(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findClientbyId(@PathVariable Long id) throws ResourceDatabaseException, ResourceNotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clientService.findClientById(id));
    }
}
