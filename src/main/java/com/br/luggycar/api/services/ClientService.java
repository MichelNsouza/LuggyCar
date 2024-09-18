package com.br.luggycar.api.services;

import ch.qos.logback.core.net.server.Client;
import com.br.luggycar.api.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
   @Autowired
   private ClientRepository clientRepository;

   public List<Client>getAll(){
       return clientRepository.findAll();
   }


}