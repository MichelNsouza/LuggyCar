package com.br.luggycar.api.services;

import com.br.luggycar.api.entities.Accident;
import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.repositories.AccidentRepository;
import com.br.luggycar.api.requests.AccidentRequest;
import com.br.luggycar.api.requests.ClientResquest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AccidentService {

    @Autowired
    private AccidentRepository accidentRepository;

    public List<Accident> getAll(){
        return accidentRepository.findAll();
    }

    public Accident insert(AccidentRequest accidentResquest) {

        Accident accident = new Accident();
        BeanUtils.copyProperties(accidentResquest, accident);

        return accidentRepository.save(accident);

    }
}
