package com.br.luggycar.api.controllers;


import com.br.luggycar.api.entities.User;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.requests.LoginRequest;
import com.br.luggycar.api.requests.RegisterRequest;
import com.br.luggycar.api.services.security.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequest loginRequest) {

        return ResponseEntity.ok(authenticationService.getUserToken(loginRequest));

    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequest registerRequest) throws ResourceNotFoundException {

        User user = authenticationService.createUser(registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("usuario " + user.getLogin() + " Criado com sucesso!");
    }
}