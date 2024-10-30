package com.br.luggycar.api.services;

import com.br.luggycar.api.dtos.requests.RentStatusRequest;
import com.br.luggycar.api.dtos.response.ClientResponse;
import com.br.luggycar.api.dtos.response.RentResponse;
import com.br.luggycar.api.dtos.response.VehicleResponse;
import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.entities.Rent;
import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.enums.rent.RentStatus;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.repositories.RentRepository;
import com.br.luggycar.api.dtos.requests.RentRequest;
import com.br.luggycar.api.utils.AuthUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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


    public RentResponse createRent(RentRequest rentRequest) {

        Rent rent = new Rent();
        BeanUtils.copyProperties(rentRequest, rent);

        rent.setStatus(IN_PROGRESS);
        rent.setCreate_at(LocalDate.now());

        ClientResponse clientResponse = clientService.findClientById(rentRequest.client().getId());
        Client client = new Client();
        BeanUtils.copyProperties(clientResponse, client);
        rent.setClient(client);

        Optional <VehicleResponse> vehicleResponseOpt = vehicleService.findVehicleById(rentRequest.vehicle().getId());

        if (vehicleResponseOpt.isPresent()) {
            VehicleResponse vehicleResponse = vehicleResponseOpt.get();

            Vehicle vehicle = new Vehicle();
            vehicle.setId(vehicleResponse.id());

            rent.setVehicle(vehicle);
        }

        String usuario = authUtil.getAuthenticatedUsername();
        rent.setUser(usuario);

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

    public RentResponse lowRent(Long id, RentStatusRequest rentStatusRequest){

        Optional<Rent> rentOpt = rentRepository.findById(id);

        if (rentOpt.isPresent()) {
            Rent rent = rentOpt.get();

            rent.setStatus(rentStatusRequest.status());
            rent.setUpdate_at(LocalDate.now());
            rentRepository.save(rent);

            return new RentResponse(rent);

        } else {
            throw new ResourceNotFoundException("Locação não encontrada.");
        }

    }

}
