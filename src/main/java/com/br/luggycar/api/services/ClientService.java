package com.br.luggycar.api.services;

import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.requests.ClientResquest;
import com.br.luggycar.api.repositories.ClientRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

   @Autowired
   private ClientRepository clientRepository;

   public List<Client> getAll(){
        return clientRepository.findAll();
    }

   public Client insert(ClientResquest clientResquest) {

       Client client = new Client();
       BeanUtils.copyProperties(clientResquest, client);

       return clientRepository.save(client);

   }
   public Optional<Client>findClientById(Long id){
       return clientRepository.findById(id);
   }

   public void deleteClientById(Long id){
        clientRepository.deleteById(id);
   }
}
