package com.br.luggycar.api.services;

import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.entities.Rent;
import com.br.luggycar.api.repositories.RentRepository;
import com.br.luggycar.api.requests.ClientResquest;
import com.br.luggycar.api.requests.RentRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class RentService {

    @Autowired
    private RentRepository rentRepository;

    public Rent creatRent(RentRequest rentRequest) {
        Rent rent = new Rent();
        BeanUtils.copyProperties(rentRequest, rent);

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
