package com.br.luggycar.api.services;
import com.br.luggycar.api.dtos.requests.ClientRequest;
import com.br.luggycar.api.dtos.response.ClientResponse;
import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.exceptions.ResourceDatabaseException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.repositories.ClientRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public ClientResponse createClient(ClientRequest clientRequest) {

        try {
            Client clientResponse = new Client();

            BeanUtils.copyProperties(clientRequest, clientResponse);

            clientResponse.setRegistration(LocalDate.now());

            clientRepository.save(clientResponse);

            return new ClientResponse(clientResponse);

        } catch (ResourceDatabaseException e) {
            throw new ResourceDatabaseException("Erro ao salvar o cliente no banco de dados", e);
        }

    }

    public List<ClientResponse> readAllClient() {

        try {

            List<Client> listClients = clientRepository.findAll();
            return listClients
                    .stream()
                    .map(ClientResponse::new)
                    .collect(Collectors.toList());

        } catch (ResourceDatabaseException e) {
            throw new ResourceDatabaseException("Erro ao Buscar clientes no banco de dados", e);
        }
    }

    public ClientResponse updateClient(Long id, ClientRequest clientRequest) throws ResourceNotFoundException {

        try {
            Client client = clientRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Sem registros de cliente com o ID: " + id
                    ));

            BeanUtils.copyProperties(client, clientRequest);

            clientRepository.save(client);

            return new ClientResponse(client);

        } catch (ResourceDatabaseException e) {
            throw new ResourceDatabaseException("Erro ao atualizar o cliente no banco de dados", e);
        }
    }

    public void deleteClient(Long id) {

        try {
            clientRepository.deleteById(id);
        } catch (ResourceDatabaseException e) {
            throw new ResourceDatabaseException("Erro ao deletar os dados do cliente no banco de dados", e);
        }
    }


    public ClientResponse findClientById(Long id) throws ResourceNotFoundException{

        try {
            Client client = clientRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Sem registros de cliente com o ID: " + id
                    ));
            return new ClientResponse(client);

        } catch (ResourceDatabaseException e) {
            throw new ResourceDatabaseException("Erro ao buscar o cliente no banco de dados", e);
        }

    }
}
