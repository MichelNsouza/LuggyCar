package com.br.luggycar.api.services;

import com.br.luggycar.api.entities.Rent;
import com.br.luggycar.api.repositories.RentRepository;
import com.br.luggycar.api.requests.RentRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RentService {

    @Autowired
    private RentRepository rentRepository;

    public List<Rent> getAll() {
        return rentRepository.findAll();
    }

    public Rent save(RentRequest rentRequest) {
        Rent rent = new Rent();
        BeanUtils.copyProperties(rentRequest, rent);

        return rentRepository.save(rent);

    }

}
