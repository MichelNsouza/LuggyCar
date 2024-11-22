package com.br.luggycar.api.services;

import com.br.luggycar.api.dtos.requests.ClientRequest;
import com.br.luggycar.api.dtos.response.ClientResponse;
import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.enums.client.PersonType;
import com.br.luggycar.api.enums.rent.RentStatus;
import com.br.luggycar.api.exceptions.*;
import com.br.luggycar.api.http.viaCepClient;
import com.br.luggycar.api.repositories.ClientRepository;
import com.br.luggycar.api.repositories.RentRepository;
import com.br.luggycar.api.utils.JWTUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private viaCepClient cephttpClient;


    public ClientResponse createClient(ClientRequest clientRequest) throws ResourceDatabaseException, ResourceExistsException, ResourceNotFoundException {

        Optional<Client> client = (clientRequest.personType() == PersonType.PF)
                ? clientRepository.findByCpf(clientRequest.cpf())
                : clientRepository.findByCnpj(clientRequest.cnpj());

        if (client.isPresent()) {
            throw new ResourceExistsException("já existe um cliente com esse documento.");
        }

        // manter 404 ou criar outro exception?
        try {
            cephttpClient.validaCep(clientRequest.cep());
        } catch (Exception e) {
            throw new ResourceNotFoundException("CEP não encontrado ou é invalido.");
        }

        Client clientResponse = new Client();

        BeanUtils.copyProperties(clientRequest, clientResponse);

        clientResponse.setRegistration(LocalDate.now());

        clientRepository.save(clientResponse);

        return new ClientResponse(clientResponse);

    }

    public List<ClientResponse> readAllClient() throws ResourceDatabaseException {

        List<Client> listClients = clientRepository.findAll();
        return listClients
                .stream()
                .map(ClientResponse::new)
                .collect(Collectors.toList());

    }

    public ClientResponse updateClient(Long id, ClientRequest clientRequest) throws ResourceNotFoundException, ResourceDatabaseException {

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sem registros de cliente com o ID: " + id
                ));

        BeanUtils.copyProperties(client, clientRequest);

        clientRepository.save(client);

        return new ClientResponse(client);

    }

    public void deleteClient(Long id) throws ResourceClientHasActiveRentalsException, ResourceNotFoundException, ResourceDatabaseException {

        ClientResponse clientResponse = findClientById(id);

        if (hasActiveRentals(id)) {
            throw new ResourceClientHasActiveRentalsException("Não é possível remover o cliente com ID " + id + " porque ele possui aluguéis ativos.");
        }

        Client client = new Client();
        BeanUtils.copyProperties(clientResponse, client);

        if (client.getPersonType() == PersonType.PF) {
            client.setNaturalPersonName(JWTUtils.generateToken(client.getNaturalPersonName()));
            client.setCpf(JWTUtils.generateToken(client.getCpf()));
        } else {
            client.setCompanyName(JWTUtils.generateToken(client.getCompanyName()));
            client.setCnpj(JWTUtils.generateToken(client.getCnpj()));
        }

        client.setEmail(JWTUtils.generateToken(client.getEmail()));
        client.setCep(JWTUtils.generateToken(client.getCep()));
        client.setEndereco(JWTUtils.generateToken(client.getEndereco()));

        clientRepository.save(client);

    }

    public ClientResponse findClientById(Long id) throws ResourceNotFoundException, ResourceDatabaseException {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        return new ClientResponse(client);
    }

    // se colocar em rentService esta dando loop
    public boolean hasActiveRentals(Long clientId) {
        List<RentStatus> activeStatuses = List.of(RentStatus.PENDING, RentStatus.IN_PROGRESS);
        return rentRepository.existsActiveRentByClientId(clientId, activeStatuses);
    }

    public boolean clientAvailable(Long id) throws ResourceDatabaseException, ResourceNotFoundException, ResourceBadRequestException {

        ClientResponse client = findClientById(id);

        if (client.personType() == PersonType.PF) {

            Date currentDate = new Date();

            if (client.drivers_license_validity().before(currentDate)) {
                throw new ResourceBadRequestException("A CNH do cliente está vencida!");
            }
            if (hasActiveRentals(client.id())) {
                throw new ResourceBadRequestException("O cliente possui aluguel pendente!");
            }

        }

        return true;

    }
}
