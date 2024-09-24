package com.br.luggycar.api.controllers;

import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.requests.ClientResquest;
import com.br.luggycar.api.services.ClientService;
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

    @GetMapping
    public ResponseEntity<List<Client>> getAll(){
        return ResponseEntity.ok(clientService.getAll());

    }

    @PostMapping("/registration")
    public ResponseEntity<Client> insert(@RequestBody ClientResquest clientResquest) {
        Client client = clientService.insert(clientResquest);

        return ResponseEntity.status(HttpStatus.CREATED).body(client);

    }



}