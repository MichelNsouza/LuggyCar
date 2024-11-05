package com.br.luggycar.api.services;

import com.br.luggycar.api.dtos.requests.CloseRentalRequest;
import com.br.luggycar.api.dtos.requests.Optional.OptionalItemRequest;
import com.br.luggycar.api.dtos.requests.Optional.OptionalQuantityRequest;
import com.br.luggycar.api.dtos.response.ClientResponse;
import com.br.luggycar.api.dtos.response.CloseRentalResponse;
import com.br.luggycar.api.dtos.response.RentResponse;
import com.br.luggycar.api.dtos.response.VehicleResponse;
import com.br.luggycar.api.entities.*;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.repositories.OptionalItemRepository;
import com.br.luggycar.api.repositories.RentOptionalRepository;
import com.br.luggycar.api.repositories.RentRepository;
import com.br.luggycar.api.dtos.requests.RentRequest;
import com.br.luggycar.api.utils.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.br.luggycar.api.enums.rent.RentStatus.IN_PROGRESS;


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
    OptionalItemService optionalItemService;
    @Autowired
    private OptionalItemRepository optionalItemRepository;

    @Transactional
    public RentResponse createRent(RentRequest rentRequest) {

        String usuario = authUtil.getAuthenticatedUsername();
        ClientResponse clientResponse = clientService.findClientById(rentRequest.clientId());
        Optional <VehicleResponse> vehicleResponseOpt = vehicleService.findVehicleById(rentRequest.vehicleId());

        // Falta implementar exceptions

        Rent rent = new Rent();
        BeanUtils.copyProperties(rentRequest, rent);

        Client client = new Client();
        BeanUtils.copyProperties(clientResponse, client);

        VehicleResponse vehicleResponse = vehicleResponseOpt.get();
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleResponse.id());

        processOptionalItems(rentRequest.optionalItems(), rent); // Utilzando método de RentOptionalItems

        rent.setVehicle(vehicle);
        rent.setUser(usuario);
        rent.setClient(client);

        Rent savedRent = rentRepository.save(rent);
        return new RentResponse(savedRent);
    }

    public List<RentResponse> readAllRent() {

        List<Rent> rents = rentRepository.findAll();

        return rents.stream()
                .map(RentResponse::new)
                .collect(Collectors.toList());
    }

    public RentResponse updateRent(Long id, RentRequest rentRequest) {

        Optional<Rent> rentOpt = rentRepository.findById(id);

        if (rentOpt.isPresent()) {
            Rent updatedRent = rentOpt.get();
            BeanUtils.copyProperties(rentRequest, updatedRent, "id", "registration");
            updatedRent.setUpdate_at(LocalDate.now());

            Rent savedRent = rentRepository.save(updatedRent);
            return new RentResponse(savedRent);
        }

        return null;

    }

    public void deleteRent(Long id){
        rentRepository.deleteById(id);
    }

    public Optional<RentResponse>findRentById(Long id){
        return rentRepository.findById(id)
                .map(RentResponse::new);

    }

    private List<RentOptionalItem> processOptionalItems(List<OptionalQuantityRequest> optionalItems, Rent rent) {
        List<RentOptionalItem> rentOptionalItems = new ArrayList<>();

        for (OptionalQuantityRequest optionalQuantityRequest : optionalItems) {
            Long idOptional = optionalQuantityRequest.id();
            Integer quantityRequested = optionalQuantityRequest.reservedQuantity();

            Optional<OptionalItem> optionalItemOpt = optionalItemService.findOptionalItemById(idOptional);

            if (optionalItemOpt.isPresent()) {
                OptionalItem optionalItem = optionalItemOpt.get();

                if (optionalItem.getQuantityAvailable() >= quantityRequested) {
                    optionalItem.setQuantityAvailable(optionalItem.getQuantityAvailable() - quantityRequested);
                    optionalItemRepository.save(optionalItem);

                    // Criar a associação na tabela intermediária
                    RentOptionalItem rentOptionalItem = new RentOptionalItem();
                    rentOptionalItem.setRent(rent);
                    rentOptionalItem.setOptionalItem(optionalItem);
                    rentOptionalItem.setReservedQuantity(quantityRequested);
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


    public CloseRentalResponse closeRental(CloseRentalRequest closeRentalRequest){

        Optional<RentResponse> rentOpt = findRentById(closeRentalRequest.id());






        return new CloseRentalResponse();



    }
}
