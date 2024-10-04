package com.br.luggycar.api.services;

import com.br.luggycar.api.entities.Accident;
import com.br.luggycar.api.repositories.AccidentRepository;
import com.br.luggycar.api.requests.AccidentRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccidentService {

    @Autowired
    private AccidentRepository accidentRepository;


    public List<Accident> getAllAccidents() {
        return accidentRepository.findAll();
    }


    public Accident findAccidentById(Long id) {
        Optional<Accident> accident = accidentRepository.findById(id);
        return accident.orElseThrow(() -> new RuntimeException("Acidente não encontrado."));
    }


    public Accident insertAccident(AccidentRequest accidentRequest) {
        Accident accident = new Accident();
        BeanUtils.copyProperties(accidentRequest, accident);
        return accidentRepository.save(accident);
    }
}
