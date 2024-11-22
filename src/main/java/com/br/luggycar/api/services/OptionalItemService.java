package com.br.luggycar.api.services;

import com.br.luggycar.api.dtos.requests.Optional.OptionalQuantityRequest;
import com.br.luggycar.api.entities.OptionalItem;
import com.br.luggycar.api.entities.rent.Rent;
import com.br.luggycar.api.entities.rent.RentOptionalItem;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.repositories.OptionalItemRepository;
import com.br.luggycar.api.dtos.requests.Optional.OptionalItemRequest;
import com.br.luggycar.api.repositories.RentOptionalRepository;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.br.luggycar.api.configsRedis.RedisConfig.PREFIXO_OPTIONAL_CACHE_REDIS;
import static com.br.luggycar.api.configsRedis.RedisConfig.PREFIXO_VEHICLE_CACHE_REDIS;


@Service
public class OptionalItemService {

    @Autowired
    private OptionalItemRepository optionalItemRepository;
    @Autowired
    private RentOptionalRepository rentOptionalRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public OptionalItem createOptionalItem(OptionalItemRequest optionalItemRequest) throws ResourceNotFoundException {

        if (optionalItemRepository.findByName(optionalItemRequest.name()).isPresent()) {
            throw new ResourceNotFoundException("Opcional já cadastrado!");
        }

        OptionalItem optionalItem = new OptionalItem();
        BeanUtils.copyProperties(optionalItemRequest, optionalItem);

        redisTemplate.delete(PREFIXO_OPTIONAL_CACHE_REDIS + "all_optionals");

        return optionalItemRepository.save(optionalItem);
    }

    public List<OptionalItem> readAllOptionalItem() {

        List<LinkedHashMap> cachedOptionalMap = (List<LinkedHashMap>) redisTemplate.opsForValue().get(PREFIXO_OPTIONAL_CACHE_REDIS + "all_optionals");

        if (cachedOptionalMap != null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.getFactory().setStreamWriteConstraints(StreamWriteConstraints.builder().maxNestingDepth(2000).build());

            List<OptionalItem> cachedOptionalItems = cachedOptionalMap.stream()
                    .map(map -> mapper.convertValue(map, OptionalItem.class))
                    .collect(Collectors.toList());

            return cachedOptionalItems;
        }

        List<OptionalItem> optionalItems = optionalItemRepository.findAll();
        redisTemplate.opsForValue().set(PREFIXO_OPTIONAL_CACHE_REDIS + " all_optionals", optionalItems, 3, TimeUnit.DAYS);

        return optionalItems;
    }

    public OptionalItem updateOptionalItem(Long id, OptionalItemRequest optionalItemRequest) {
        Optional<OptionalItem> optionalItem = findOptionalItemById(id);

        if (optionalItem.isPresent()) {
            OptionalItem optionalItemToUpdate = optionalItem.get();
            BeanUtils.copyProperties(optionalItemRequest, optionalItemToUpdate);

            redisTemplate.delete(PREFIXO_VEHICLE_CACHE_REDIS + "all_rents");

            return optionalItemRepository.save(optionalItemToUpdate);
        }

        return null;
    }

    public boolean deleteOptionalItem(Long id) {
        Optional<OptionalItem> optionalItem = findOptionalItemById(id);

        if (optionalItem.isPresent()) {
            optionalItemRepository.delete(optionalItem.get());

            redisTemplate.delete(PREFIXO_VEHICLE_CACHE_REDIS + "all_rents");

            return true;
        }

        return false;
    }

    public Optional<OptionalItem> findOptionalItemById(Long id) {

        return optionalItemRepository.findById(id);
    }


    @Transactional
    public List<RentOptionalItem> processAddOptionalItems(List<OptionalQuantityRequest> optionalItems, Rent rent) throws ResourceNotFoundException {

        List<RentOptionalItem> rentOptionalItems = new ArrayList<>();

        for (OptionalQuantityRequest optionalQuantityRequest : optionalItems) {

            Long idOptional = optionalQuantityRequest.id();
            Integer quantityRequested = optionalQuantityRequest.quantity();

            Optional<OptionalItem> optionalItemOpt = findOptionalItemById(idOptional);

            if (optionalItemOpt.isPresent()) {
                OptionalItem optionalItem = optionalItemOpt.get();

                if (optionalItem.getQuantityAvailable() >= quantityRequested) {
                    optionalItem.setQuantityAvailable(optionalItem.getQuantityAvailable() - quantityRequested);
                    optionalItemRepository.save(optionalItem);

                    // Criar e associar RentOptionalItem
                    RentOptionalItem rentOptionalItem = new RentOptionalItem();
                    rentOptionalItem.setRent(rent);  // Associar o Rent já salvo
                    rentOptionalItem.setOptionalItem(optionalItem);
                    rentOptionalItem.setQuantity(quantityRequested);

                    rentOptionalRepository.save(rentOptionalItem);
                    rentOptionalItems.add(rentOptionalItem);

                } else {
                    throw new ResourceNotFoundException("Optional item ID " + idOptional + " não tem quantidade suficiente disponível");
                }
            } else {
                throw new ResourceNotFoundException("Optional item ID " + idOptional + " não encontrada.");
            }
        }

        return rentOptionalItems;
    }

    @Transactional
    public Double processTotalOptionalItems(List<RentOptionalItem> rentOptionalItems) {

        Double total = 0.0;

        for (RentOptionalItem rentOptionalItem : rentOptionalItems) {

            total = total + rentOptionalItem.calculateTotal();

        }


        return total;
    }
}
