package com.br.luggycar.api.controllers;

import ch.qos.logback.core.net.server.Client;
import com.br.luggycar.api.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/client")



public class ClientController {
    @Autowired
    private ClientService clientService;



    @GetMapping
    public ResponseEntity <List<Client>> getAll(){
        return ResponseEntity.ok(clientService.getAll());


    }


}