package com.br.luggycar.api.services.security;

import com.br.luggycar.api.entities.User;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.repositories.UserRepository;
import com.br.luggycar.api.requests.LoginRequest;
import com.br.luggycar.api.requests.RegisterRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    public String getUserToken(LoginRequest loginRequest) {

        if (loginRequest.login() == null || loginRequest.password() == null) {
            throw new IllegalArgumentException("Login e senha não podem ser nulos");
        }

        var usernamePassword = new UsernamePasswordAuthenticationToken(loginRequest.login(), loginRequest.password());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return token;
    }

    public User createUser(RegisterRequest registerRequest) throws ResourceNotFoundException {

        if (this.userRepository.findByLogin(registerRequest.login()) != null) {
            throw new ResourceNotFoundException("Usuario já existe");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerRequest.password());

        User user = new User(registerRequest.login(), encryptedPassword, registerRequest.role());
        BeanUtils.copyProperties(registerRequest, user);

        return userRepository.save(user);
    }



}
