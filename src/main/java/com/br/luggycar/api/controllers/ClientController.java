package com.br.luggycar.api.controllers;

import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.requests.ClientResquest;
import com.br.luggycar.api.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody ClientResquest clientResquest) {
        Client client = clientService.createClient(clientResquest);

        return ResponseEntity.status(HttpStatus.CREATED).body(client);

    }

    @GetMapping
    public ResponseEntity<List<Client>> readAllClient(){
        return ResponseEntity.ok(clientService.readAllClient());

    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody ClientResquest clientResquest) throws ResourceNotFoundException {

            Optional<Client> client = clientService.findClientById(id);

            if (client.isEmpty()) {
                  throw new ResourceNotFoundException("Cliente não encontrado!");
            }

            Client clientResponse = clientService.updateClient(id, clientResquest);

            return ResponseEntity.ok().body(clientResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Client> deleteClient(@PathVariable Long id){
       clientService.deleteClient(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientbyId(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Client> client = clientService.findClientById(id);

        if (client.isEmpty())  {
            throw new ResourceNotFoundException("Cliente não encontrado!");
        }

        return ResponseEntity.ok().body(client.get());

    }


}
