package com.br.luggycar.api.services;

import com.br.luggycar.api.dtos.requests.rent.CloseRentalRequest;
import com.br.luggycar.api.dtos.requests.Optional.OptionalQuantityRequest;
import com.br.luggycar.api.dtos.requests.rent.RentRequestUpdate;
import com.br.luggycar.api.dtos.response.ClientResponse;
import com.br.luggycar.api.dtos.response.rent.CloseRentalResponse;
import com.br.luggycar.api.dtos.response.rent.RentResponse;
import com.br.luggycar.api.dtos.response.VehicleResponse;
import com.br.luggycar.api.entities.*;
import com.br.luggycar.api.enums.rent.RentStatus;
import com.br.luggycar.api.exceptions.ResourceExistsException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.repositories.OptionalItemRepository;
import com.br.luggycar.api.repositories.RentOptionalRepository;
import com.br.luggycar.api.repositories.RentRepository;
import com.br.luggycar.api.dtos.requests.rent.RentRequest;
import com.br.luggycar.api.utils.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentService {

    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private RentOptionalRepository rentOptionalRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private OptionalItemService optionalItemService;

    @Autowired
    private OptionalItemRepository optionalItemRepository;

    @Transactional
    public RentResponse createRent(RentRequest rentRequest) {

        // user, client e veiculo
        String usuario = authUtil.getAuthenticatedUsername();
        ClientResponse clientResponse = clientService.findClientById(rentRequest.clientId());

        List<RentStatus> activeStatuses = Arrays.asList(RentStatus.IN_PROGRESS, RentStatus.PENDING);

        if (vehicleService.isVehicleAvailable(rentRequest.vehicleId(), activeStatuses)){
            throw new ResourceExistsException("O veiculo com ID: "+ rentRequest.vehicleId() + " possui locação Em andamento, ou pendente");
        }

        Optional<VehicleResponse> vehicleResponseOpt = vehicleService.findVehicleById(rentRequest.vehicleId());

        // Criando as entidades
        Rent rent = new Rent();
        BeanUtils.copyProperties(rentRequest, rent);
        Client client = new Client();
        BeanUtils.copyProperties(clientResponse, client);
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleResponseOpt.get().id());

        // Processando os itens opcionais
        processAddOptionalItems(rentRequest.optionalItems(), rent);

        rent.setVehicle(vehicle);
        rent.setUser(usuario);
        rent.setClient(client);

        Rent savedRent = rentRepository.save(rent);

        return new RentResponse(savedRent);
    }

    public List<RentResponse> readAllRent() {
        List<Rent> rents = rentRepository.findAll();
        return rents.stream().map(RentResponse::new).collect(Collectors.toList());
    }

    public RentResponse updateRent(Long id, RentRequestUpdate rentRequest) {
        Optional<Rent> rentOpt = rentRepository.findById(id);

        if (rentOpt.isPresent()) {
            Rent updatedRent = rentOpt.get();
            BeanUtils.copyProperties(rentRequest, updatedRent, "id");
            updatedRent.setUpdate_at(LocalDate.now());
            Rent savedRent = rentRepository.save(updatedRent);
            return new RentResponse(savedRent);
        }

        throw new ResourceNotFoundException("Rent not found with ID: " + id);
    }

    public void deleteRent(Long id) {
        rentRepository.deleteById(id);
    }

    public Optional<RentResponse> findRentById(Long id) {
        return rentRepository.findById(id).map(RentResponse::new);
    }

    public CloseRentalResponse closeRental(CloseRentalRequest closeRentalRequest) {

        Rent rent = rentRepository.findById(closeRentalRequest.id())
                .orElseThrow(() -> new ResourceNotFoundException("Alugel não encontrado!"));

        if (rent.getStatus() != RentStatus.COMPLETED) {
            for (RentOptionalItem rentOptionalItem : rent.getRentOptionalItems()) {
                OptionalItem item = rentOptionalItem.getOptionalItem();
                item.setQuantityAvailable(item.getQuantityAvailable() + rentOptionalItem.getQuantity());
                optionalItemRepository.save(item);
            }

            // Aqui adicionar lógica para calcular o valor total, etc.

            RentRequestUpdate rentRequestUpdate = new RentRequestUpdate();

            rentRequestUpdate.setStatus(RentStatus.COMPLETED);
            // rentRequestUpdate.getKmFinal() receber do close rent
            // e mais coisas de rent


            updateRent(rent.getId(), rentRequestUpdate);

            return new CloseRentalResponse("Teste, rent fechado");
        }
        throw new RuntimeException("Não é possivel finalizar um aluguel ja concluido");
    }

    private List<RentOptionalItem> processAddOptionalItems(List<OptionalQuantityRequest> optionalItems, Rent rent) {

        List<RentOptionalItem> rentOptionalItems = new ArrayList<>();

        for (OptionalQuantityRequest optionalQuantityRequest : optionalItems) {

            Long idOptional = optionalQuantityRequest.id();

            Integer quantityRequested = optionalQuantityRequest.quantity();

            Optional<OptionalItem> optionalItemOpt = optionalItemService.findOptionalItemById(idOptional);

            if (optionalItemOpt.isPresent()) {

                OptionalItem optionalItem = optionalItemOpt.get();

                if (optionalItem.getQuantityAvailable() >= quantityRequested) {

                    optionalItem.setQuantityAvailable(optionalItem.getQuantityAvailable() - quantityRequested);

                    optionalItemRepository.save(optionalItem);

                    //  tabela intermediária
                    RentOptionalItem rentOptionalItem = new RentOptionalItem();

                    rentOptionalItem.setRent(rent);
                    rentOptionalItem.setOptionalItem(optionalItem);
                    rentOptionalItem.setQuantity(quantityRequested);

                    rentOptionalRepository.save(rentOptionalItem);

                    rentOptionalItems.add(rentOptionalItem);

                } else {
                    throw new ResourceNotFoundException("Optional item ID " + idOptional + " não tem quantidade suficiente disponível");
                }
            } else {
                throw new ResourceNotFoundException("Optional item ID " + idOptional + " não encontrada.");
            }
        }

        return rentOptionalItems;
    }

//passar logica de remover para ca
//    private List<RentOptionalItem> processRemoveOptionalItems(List<OptionalQuantityRequest> optionalItems, Rent rent){
//
//    }


}
