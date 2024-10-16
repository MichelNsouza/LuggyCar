package com.br.luggycar.api.services;

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


    public Rent createRent(RentRequest rentRequest) {

        Rent rent = new Rent();
        BeanUtils.copyProperties(rentRequest, rent);

        rent.setRegistration(LocalDate.now());

        Optional <Client> client = clientService.findClientById(rentRequest.client().getId());
        rent.setClient(client.get());

        // Quebrando essa parte do código por causa do método findVehicleById que está no VehicleService
        Optional <Vehicle> vehicle = vehicleService.findVehicleById(rentRequest.vehicle().getId());
        rent.setVehicle(vehicle.get());

        String usuario = authUtil.getAuthenticatedUsername();
        rent.setUser(usuario);

        System.out.println(rent);

        return rentRepository.save(rent);

    }

    public List<Rent> readAllRent() {
        return rentRepository.findAll();
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
