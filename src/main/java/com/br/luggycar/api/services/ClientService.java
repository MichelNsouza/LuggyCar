package com.br.luggycar.api.services;

import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.requests.ClientResquest;
import com.br.luggycar.api.repositories.ClientRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

   @Autowired
   private ClientRepository clientRepository;

    public Client createClient(ClientResquest clientResquest) {

        Client client = new Client();
        BeanUtils.copyProperties(clientResquest, client);

        client.setRegistration(LocalDate.now());

        return clientRepository.save(client);

    }

    public List<Client> readAllClient(){
        return clientRepository.findAll();
    }

   public Client updateClient(Long id, ClientResquest clientResquest) {

       Optional<Client> client = findClientById(id);

       if (client.isPresent()) {
           Client updatedClient = client.get();
           BeanUtils.copyProperties(clientResquest, updatedClient);
           return clientRepository.save(updatedClient);
       }

       return null;
   }

   public void deleteClient(Long id){
        clientRepository.deleteById(id);
   }


    public Optional<Client>findClientById(Long id){
        return clientRepository.findById(id);
    }
}
