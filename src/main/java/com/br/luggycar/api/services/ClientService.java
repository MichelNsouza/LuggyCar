package com.br.luggycar.api.services;
import com.br.luggycar.api.dtos.requests.ClientRequest;
import com.br.luggycar.api.dtos.response.ClientResponse;
import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.enums.client.PersonType;
import com.br.luggycar.api.enums.rent.RentStatus;
import com.br.luggycar.api.exceptions.*;
import com.br.luggycar.api.http.viaCepClient;
import com.br.luggycar.api.repositories.ClientRepository;
import com.br.luggycar.api.repositories.RentRepository;
import com.br.luggycar.api.utils.JWTUtils;
import jakarta.validation.constraints.Email;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;


@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private viaCepClient cephttpClient;


    public ClientResponse createClient(ClientRequest clientRequest) {

//        if (clientRequest.personType() == null) {
//            throw new ResourceNullException("O campo 'personType' não pode ser nulo.");
//        }

        Optional<Client> client = (clientRequest.personType() == PersonType.PF)
                ? clientRepository.findByCpf(clientRequest.cpf())
                : clientRepository.findByCnpj(clientRequest.cnpj());

        if (client.isPresent()) {
            throw new ResourceExistsException("já existe um cliente com esse documento.");
        }

         // manter 404 ou criar outro exception?
        try {
            cephttpClient.validaCep(clientRequest.cep());
        }catch (Exception e){
            throw new ResourceNotFoundException("CEP não encontrado ou é invalido.");
        }

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

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sem registros de cliente com o ID: " + id
                ));

        //não esta funcionando
        if (hasActiveRentals(id)) {
            throw new ResourceClientHasActiveRentalsException("Não é possível remover o cliente com ID " + id + " porque ele possui aluguéis ativos.");
        }

        try {
            if (client.getPersonType() == PersonType.PF) {
                client.setNaturalPersonName(JWTUtils.generateToken(client.getNaturalPersonName()));
                client.setCpf(JWTUtils.generateToken(client.getCpf()));
            }else {
                client.setCompanyName(JWTUtils.generateToken(client.getCompanyName()));
                client.setCnpj(JWTUtils.generateToken(client.getCnpj()));
            }

            client.setEmail(JWTUtils.generateToken(client.getEmail()));
            client.setCep(JWTUtils.generateToken(client.getCep()));
            client.setEndereco(JWTUtils.generateToken(client.getEndereco()));

            clientRepository.save(client);

        } catch (ResourceDatabaseException e) {
            throw new ResourceDatabaseException("Erro ao pseudonimizar os dados do cliente no banco de dados", e);
        }
    }

    public boolean hasActiveRentals(Long clientId) {
        return rentRepository.existsActiveRentByClientId(clientId, RentStatus.IN_PROGRESS);
    }

    public ClientResponse findClientById(Long id) throws ResourceNotFoundException {
        try {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        return new ClientResponse(client);
        } catch (ResourceDatabaseException e) {
            throw new ResourceDatabaseException("Erro ao buscar o cliente no banco de dados", e);
        }
    }

}
