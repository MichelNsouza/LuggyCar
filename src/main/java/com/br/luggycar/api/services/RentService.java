package com.br.luggycar.api.services;

import com.br.luggycar.api.dtos.requests.CloseRentalRequest;
import com.br.luggycar.api.dtos.response.ClientResponse;
import com.br.luggycar.api.dtos.response.CloseRentalResponse;
import com.br.luggycar.api.dtos.response.RentResponse;
import com.br.luggycar.api.dtos.response.VehicleResponse;
import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.entities.OptionalItem;
import com.br.luggycar.api.entities.Rent;
import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.repositories.OptionalItemRepository;
import com.br.luggycar.api.repositories.RentRepository;
import com.br.luggycar.api.dtos.requests.RentRequest;
import com.br.luggycar.api.utils.AuthUtil;
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
    private AuthUtil authUtil;
    @Autowired
    OptionalItemService optionalItemService;
    @Autowired
    private OptionalItemRepository optionalItemRepository;


    public RentResponse createRent(RentRequest rentRequest) {

        String usuario = authUtil.getAuthenticatedUsername();
        ClientResponse clientResponse = clientService.findClientById(rentRequest.clientId());
        Optional <VehicleResponse> vehicleResponseOpt = vehicleService.findVehicleById(rentRequest.vehicleId());


        if (clientResponse == null) {
            throw new ResourceNotFoundException("Client ID " + rentRequest.clientId() + " not found.");
        }


        if (vehicleResponseOpt.isEmpty()) {
            throw new ResourceNotFoundException("Vehicle ID " + rentRequest.vehicleId() + " not found.");
        }

        Rent rent = new Rent();
        BeanUtils.copyProperties(rentRequest, rent);

        Client client = new Client();
        BeanUtils.copyProperties(clientResponse, client);

        VehicleResponse vehicleResponse = vehicleResponseOpt.get();
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleResponse.id());

        List<OptionalItem> optionalItems = new ArrayList<>();

        // Iteração chave-valor do map de opcionais contido no rentRequest
        // Long -> ID do item opcional
        // Integer -> quantidade que deseja solicitar
        for (Map.Entry<Long, Integer> entry : rentRequest.optionalItems().entrySet()) {
            Long idOptional = entry.getKey(); // id do item opcional
            Integer quantityRequested = entry.getValue(); // Quantidade solicitada

            Optional<OptionalItem> optionalItemOpt = optionalItemService.findOptionalItemById(idOptional);

            if (optionalItemOpt.isPresent()) {
                OptionalItem optionalItem = optionalItemOpt.get();

                if (optionalItem.getQuantityAvailable() >= quantityRequested) {
                    optionalItem.setQuantityAvailable(optionalItem.getQuantityAvailable() - quantityRequested);
                    optionalItemRepository.save(optionalItem);
                    optionalItems.add(optionalItem);
                } else {
                    throw new ResourceNotFoundException("Optional item ID " + idOptional + " não tem quantidade suficiente disponível");
                }
            } else {
                throw new ResourceNotFoundException("Optional item ID " + idOptional + " not found.");
            }
        }

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

    public CloseRentalResponse closeRental(CloseRentalRequest closeRentalRequest){

        Optional<RentResponse> rentOpt = findRentById(closeRentalRequest.id());






        return new CloseRentalResponse();



    }
}
