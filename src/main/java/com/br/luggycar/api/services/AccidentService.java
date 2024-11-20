package com.br.luggycar.api.services;

import com.br.luggycar.api.entities.Accident;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.repositories.AccidentRepository;
import com.br.luggycar.api.dtos.requests.AccidentRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Service
public class AccidentService {

    @Autowired
    private AccidentRepository accidentRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    private static final String PREFIXO_CACHE_REDIS = "accident:";

    public Accident createAccident(AccidentRequest accidentRequest) {

        if (accidentRepository.findByDescription(accidentRequest.description()).isPresent()) {
            throw new ResourceNotFoundException("sinistro já cadastrado!");
        }

        Accident accident = new Accident();

        BeanUtils.copyProperties(accidentRequest, accident);

        redisTemplate.delete(PREFIXO_CACHE_REDIS + "all_accidents");

        return accidentRepository.save(accident);
    }

    public List<Accident> readAllAccident() {
        String cacheKey = PREFIXO_CACHE_REDIS + "all_accidents";

        List<Accident> cachedAccidents = (List<Accident>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedAccidents == null) {
            List<Accident> accidents = accidentRepository.findAll();
            redisTemplate.opsForValue().set(cacheKey, (Accident) accidents, 3, TimeUnit.DAYS);
            return accidents;
        }

        return cachedAccidents;
    }

    public Accident updateAccident(Long id, AccidentRequest accidentRequest) {
        Optional<Accident> accident = accidentRepository.findById(id);

        if (accident.isPresent()) {

            Accident accidentToUpdate = accident.get();

            BeanUtils.copyProperties(accidentRequest, accidentToUpdate);

            Accident updatedAccident = accidentRepository.save(accidentToUpdate);

            redisTemplate.delete(PREFIXO_CACHE_REDIS + id);
            redisTemplate.delete(PREFIXO_CACHE_REDIS + "all_accidents");

            return updatedAccident;
        }

        throw new ResourceNotFoundException("Acidente não encontrado com o ID: " + id);
    }


    public boolean deleteAccident(Long id) {
        Optional<Accident> accident = accidentRepository.findById(id);

        if (accident.isPresent()) {
            accidentRepository.delete(accident.get());

            redisTemplate.delete(PREFIXO_CACHE_REDIS + id);
            redisTemplate.delete(PREFIXO_CACHE_REDIS + "all_accidents");

            return true;
        }
        throw new ResourceNotFoundException("Acidente não encontrado com o ID: " + id);
    }



    public Accident findAccidentById(Long id) {

            Accident accident = accidentRepository.findById(id).orElseThrow(()
             -> new ResourceNotFoundException("Acidente não encontrado com o ID: " + id));

            return accident;



    }

}
