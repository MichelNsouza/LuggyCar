package com.br.luggycar.api.services;

import com.br.luggycar.api.entities.Accident;
import com.br.luggycar.api.repositories.AccidentRepository;
import com.br.luggycar.api.dtos.requests.AccidentRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AccidentService {

    @Autowired
    private AccidentRepository accidentRepository;

    public Accident createAccident(AccidentRequest accidentRequest) {

        Accident accident = new Accident();

        BeanUtils.copyProperties(accidentRequest, accident);

        return accidentRepository.save(accident);
    }

    public List<Accident> readAllAccident() {

        return accidentRepository.findAll();

    }

    public Accident updateAccident(Long id, AccidentRequest accidentRequest) {

        Optional<Accident> accident = accidentRepository.findById(id);

        if (accident.isPresent()) {

            Accident accidentToUpdate = accident.get();

            BeanUtils.copyProperties(accidentRequest, accidentToUpdate);

            return accidentRepository.save(accidentToUpdate);
        }

        return null;
    }

    public boolean deleteAccident(Long id) {

        Optional<Accident> accident = accidentRepository.findById(id);

        if (accident.isPresent()) {
            accidentRepository.delete(accident.get());
            return true;
        }

        return false;
    }


    public Accident findAccidentById(Long id) {
        Optional<Accident> accident = accidentRepository.findById(id);
        return accident.orElseThrow(() -> new RuntimeException("Acidente não encontrado."));
    }
}
