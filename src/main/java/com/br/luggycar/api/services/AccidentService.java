package com.br.luggycar.api.services;

import com.br.luggycar.api.entities.Accident;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.repositories.AccidentRepository;
import com.br.luggycar.api.dtos.requests.AccidentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.br.luggycar.api.configsRedis.RedisConfig.PREFIXO_ACCIDENT_CACHE_REDIS;

@Service
public class AccidentService {

    @Autowired
    private AccidentRepository accidentRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Accident createAccident(AccidentRequest accidentRequest) throws ResourceNotFoundException {

        if (accidentRepository.findByDescription(accidentRequest.description()).isPresent()) {
            throw new ResourceNotFoundException("sinistro já cadastrado!");
        }

        Accident accident = new Accident();

        BeanUtils.copyProperties(accidentRequest, accident);

        redisTemplate.delete(PREFIXO_ACCIDENT_CACHE_REDIS + "all_accidents");

        return accidentRepository.save(accident);
    }

    public List<Accident> readAllAccident() {

        List<LinkedHashMap> cachedAccidentMap = (List<LinkedHashMap>) redisTemplate.opsForValue().get(PREFIXO_ACCIDENT_CACHE_REDIS);

        if (cachedAccidentMap != null) {

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());


            List<Accident> cachedAccident = cachedAccidentMap.stream()
                    .map(map -> mapper.convertValue(map, Accident.class))
                    .collect(Collectors.toList());

            return cachedAccident.stream().collect(Collectors.toList());
        }

        List<Accident> cachedAccidents = (List<Accident>) redisTemplate.opsForValue().get(PREFIXO_ACCIDENT_CACHE_REDIS + "all_accidents");
        if (cachedAccidents == null) {
            List<Accident> accidents = accidentRepository.findAll();
            redisTemplate.opsForValue().set(PREFIXO_ACCIDENT_CACHE_REDIS + "all_accidents", accidents, 3, TimeUnit.DAYS);
            return accidents;
        }

        return cachedAccidents;
    }

    public Accident updateAccident(Long id, AccidentRequest accidentRequest) throws ResourceNotFoundException {
        Optional<Accident> accident = accidentRepository.findById(id);

        if (accident.isPresent()) {

            Accident accidentToUpdate = accident.get();

            BeanUtils.copyProperties(accidentRequest, accidentToUpdate);

            Accident updatedAccident = accidentRepository.save(accidentToUpdate);

            redisTemplate.delete(PREFIXO_ACCIDENT_CACHE_REDIS + "all_accidents");

            return updatedAccident;
        }

        throw new ResourceNotFoundException("Acidente não encontrado com o ID: " + id);
    }


    public boolean deleteAccident(Long id) throws ResourceNotFoundException {
        Optional<Accident> accident = accidentRepository.findById(id);

        if (accident.isPresent()) {
            accidentRepository.delete(accident.get());

            redisTemplate.delete(PREFIXO_ACCIDENT_CACHE_REDIS + "all_accidents");

            return true;
        }
        throw new ResourceNotFoundException("Acidente não encontrado com o ID: " + id);
    }


    public Accident findAccidentById(Long id) throws ResourceNotFoundException {

        Accident accident = accidentRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Acidente não encontrado com o ID: " + id));

        return accident;


    }

}
