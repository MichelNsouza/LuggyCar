package com.br.luggycar.api.services;

import com.br.luggycar.api.dtos.requests.Optional.OptionalQuantityRequest;
import com.br.luggycar.api.entities.OptionalItem;
import com.br.luggycar.api.entities.rent.Rent;
import com.br.luggycar.api.entities.rent.RentOptionalItem;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.repositories.OptionalItemRepository;
import com.br.luggycar.api.dtos.requests.Optional.OptionalItemRequest;
import com.br.luggycar.api.repositories.RentOptionalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OptionalItemService {

    @Autowired
    private OptionalItemRepository optionalItemRepository;
    @Autowired
    private RentOptionalRepository rentOptionalRepository;

    public OptionalItem createOptionalItem(OptionalItemRequest optionalItemRequest) {

        if (optionalItemRepository.findByName(optionalItemRequest.name()).isPresent()) {
            throw new ResourceNotFoundException("Opcional já cadastrado!");
        }

        OptionalItem optionalItem = new OptionalItem();
        BeanUtils.copyProperties(optionalItemRequest, optionalItem);
        return optionalItemRepository.save(optionalItem);
    }

    public List<OptionalItem> readAllOptionalItem() {
        return optionalItemRepository.findAll();
    }

    public OptionalItem updateOptionalItem(Long id, OptionalItemRequest optionalItemRequest) {
        Optional<OptionalItem> optionalItem = findOptionalItemById(id);

        if (optionalItem.isPresent()) {
            OptionalItem optionalItemToUpdate = optionalItem.get();
            BeanUtils.copyProperties(optionalItemRequest, optionalItemToUpdate);
            return optionalItemRepository.save(optionalItemToUpdate);
        }

        return null;
    }

    public boolean deleteOptionalItem(Long id) {
        Optional<OptionalItem> optionalItem = findOptionalItemById(id);

        if (optionalItem.isPresent()) {
            optionalItemRepository.delete(optionalItem.get());
            return true;
        }

        return false;
    }

    public Optional<OptionalItem> findOptionalItemById(Long id) {
        return optionalItemRepository.findById(id);
    }


    @Transactional
    public List<RentOptionalItem> processAddOptionalItems(List<OptionalQuantityRequest> optionalItems, Rent rent) {

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
    public Double processTotalOptionalItems(List<RentOptionalItem> rentOptionalItems){

        Double total = 0.0;

        for (RentOptionalItem rentOptionalItem : rentOptionalItems) {

            total = total + rentOptionalItem.calculateTotal();

        }


        return total;
    }
}
