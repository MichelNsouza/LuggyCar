package com.br.luggycar.api.services;

import com.br.luggycar.api.dtos.response.RentResponse;
import com.br.luggycar.api.dtos.response.VehicleResponse;
import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.entities.Rent;
import com.br.luggycar.api.entities.Vehicle;
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

        rent.setRegistration(LocalDate.now());

        Optional <Client> client = clientService.findClientById(rentRequest.client().getId());
        rent.setClient(client.get());

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

    public Rent updateRent(Long id, RentRequest rentRequest) {

        Optional<Rent> rent = findRentById(id);

        if (rent.isPresent()) {
            Rent updatedRent = rent.get();
            BeanUtils.copyProperties(rentRequest, updatedRent);
            return rentRepository.save(updatedRent);
        }

        return null;
    }

    public void deleteRent(Long id){
        rentRepository.deleteById(id);
    }


    public Optional<Rent>findRentById(Long id){
        return rentRepository.findById(id);
    }

}
