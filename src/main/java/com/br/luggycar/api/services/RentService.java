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
import com.br.luggycar.api.repositories.RentRepository;
import com.br.luggycar.api.dtos.requests.RentRequest;
import com.br.luggycar.api.utils.AuthUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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


    public RentResponse createRent(RentRequest rentRequest) {

        String usuario = authUtil.getAuthenticatedUsername();
        ClientResponse clientResponse = clientService.findClientById(rentRequest.clientId());
        Optional <VehicleResponse> vehicleResponseOpt = vehicleService.findVehicleById(rentRequest.vehicleId());

        Rent rent = new Rent();
        BeanUtils.copyProperties(rentRequest, rent);

        Client client = new Client();
        BeanUtils.copyProperties(clientResponse, client);

        VehicleResponse vehicleResponse = vehicleResponseOpt.get();
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleResponse.id());

        List<Optional<OptionalItem>> OptionalItems = new ArrayList<>();

        for (Long idOptional : rentRequest.optionalItemIds()) {
            OptionalItems.add(optionalItemService.findOptionalItemById(idOptional));
            // fazer soma opt aqui
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
