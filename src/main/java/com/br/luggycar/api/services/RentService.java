package com.br.luggycar.api.services;

import com.br.luggycar.api.dtos.requests.rent.CloseRentalRequest;
import com.br.luggycar.api.dtos.requests.rent.RentRequestUpdate;
import com.br.luggycar.api.dtos.response.ClientResponse;
import com.br.luggycar.api.dtos.response.VehicleResponse;
import com.br.luggycar.api.dtos.response.rent.CloseRentalResponse;
import com.br.luggycar.api.dtos.response.rent.RentResponse;
import com.br.luggycar.api.entities.*;
import com.br.luggycar.api.enums.rent.RentStatus;
import com.br.luggycar.api.exceptions.ResourceBadRequestException;
import com.br.luggycar.api.exceptions.ResourceDatabaseException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.repositories.*;
import com.br.luggycar.api.dtos.requests.rent.RentRequest;
import com.br.luggycar.api.utils.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    private OptionalItemService optionalItemService;
    @Autowired
    private OptionalItemRepository optionalItemRepository;


    @Transactional
    public RentResponse createRent(RentRequest rentRequest) {
        try {
            clientService.clientAvailable(rentRequest.clientId());
            vehicleService.isVehicleAvailableById(rentRequest.vehicleId());

            String usuario = authUtil.getAuthenticatedUsername();
            ClientResponse clientResponse = clientService.findClientById(rentRequest.clientId());
            Optional<VehicleResponse> vehicleResponse = vehicleService.findVehicleById(rentRequest.vehicleId());

            Client client = new Client();
            BeanUtils.copyProperties(clientResponse, client);
            Vehicle vehicle = new Vehicle();
            BeanUtils.copyProperties(vehicleResponse.get(), vehicle);
            Rent rent = new Rent();
            BeanUtils.copyProperties(rentRequest, rent);

            rent.setUser(usuario);
            rent.setClient(client);
            rent.setVehicle(vehicle);

            rent = rentRepository.save(rent);

            List<RentOptionalItem> rentOptionalItems = optionalItemService.processAddOptionalItems(rentRequest.optionalItems(), rent);
            rent.setRentOptionalItems(rentOptionalItems);

            rentRepository.save(rent);

            return new RentResponse(rent);

        }catch (ResourceBadRequestException e){
            throw new ResourceBadRequestException("Algo deu errado! " + e.getMessage());
        }

    }

    @Transactional
    public List<RentResponse> readAllRent() {
        try {
            List<Rent> rents = rentRepository.findAll();
            return rents
                    .stream()
                    .map(RentResponse::new)
                    .collect(Collectors.toList());

        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Algo deu errado! " + e.getMessage());
        }
    }

    @Transactional
    public RentResponse updateRent(Long id, RentRequestUpdate rentRequest) {
        Optional<Rent> rentOpt = rentRepository.findById(id);
        try {

            Rent updatedRent = rentOpt.get();

            BeanUtils.copyProperties(rentRequest, updatedRent, "id");

            updatedRent.setUpdate_at(LocalDate.now());

            Rent savedRent = rentRepository.save(updatedRent);

            return new RentResponse(savedRent);

        }catch (ResourceBadRequestException e){
            throw new ResourceBadRequestException("Algo deu errado! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteRent(Long id) {
        try {
            rentRepository.deleteById(id);
        }catch (ResourceDatabaseException e){
            throw new ResourceDatabaseException("Algo deu errado!", e);
        }
    }

    @Transactional
    public Optional<RentResponse> findRentById(Long id) {
        try {
            return rentRepository.findById(id)
                    .map(RentResponse::new);
        }catch (ResourceNotFoundException e){
        throw new ResourceNotFoundException("Aluguel não encontrado! " + e.getMessage());
        }
    }

    @Transactional
    public List<RentResponse> findAllRentByClientId(Long id) {
        // Verifique se o cliente existe (ou lance a exceção, se necessário)
        clientService.findClientById(id);

        try {

            List<Rent> rents = rentRepository.findByClientId(id);

            List<RentResponse> rentResponses = rents.stream()
                    .map(rent -> {
                        RentResponse rentResponse = new RentResponse(rent);
                        return rentResponse;
                    })
                    .collect(Collectors.toList());

            return rentResponses;

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("O Cliente não possui locações cadastradas! " + e.getMessage());
        }
    }

    @Transactional
    public CloseRentalResponse closeRental(CloseRentalRequest closeRentalRequest) {

        Rent rent = rentRepository.findById(closeRentalRequest.id())
                .orElseThrow(() -> new ResourceNotFoundException("Alugel não encontrado!"));

        if (rent.getStatus() == RentStatus.COMPLETED) {
            throw new RuntimeException("Não é possível finalizar um aluguel já concluído");
        }

        // Atualiza a quantidade de itens opcionais
        for (RentOptionalItem rentOptionalItem : rent.getRentOptionalItems()) {
            OptionalItem item = rentOptionalItem.getOptionalItem();
            item.setQuantityAvailable(item.getQuantityAvailable() + rentOptionalItem.getQuantity());
            optionalItemRepository.save(item);
        }

        // Calcular o valor total do aluguel (base + itens opcionais)
        BigDecimal valorTotal = calcularValorFinal(rent);

        // Atualiza os detalhes do aluguel
        RentRequestUpdate rentRequestUpdate = new RentRequestUpdate();
        rentRequestUpdate.setStatus(RentStatus.COMPLETED);
        rentRequestUpdate.setKmFinal(closeRentalRequest.kmFinal()); // Atualiza o km final
        rentRequestUpdate.setTotalValue(valorTotal); // Atualiza o valor total

        updateRent(rent.getId(), rentRequestUpdate);

        return new CloseRentalResponse("Aluguel finalizado com sucesso!");
    }

    private BigDecimal calcularValorFinal(Rent rent) {
        // Certifique-se de que rent.getDailyRate() é BigDecimal, e converta totalDays para BigDecimal
        BigDecimal valorBase = rent.getDailyRate().multiply(BigDecimal.valueOf(rent.getTotalDays()));

        // Calcular o valor dos itens extras, garantindo que os valores sejam BigDecimal
        BigDecimal valorExtras = rent.getRentOptionalItems().stream()
                .map(item -> {
                    // Garantir que quantity e rentalValue sejam BigDecimal
                    BigDecimal quantity = new BigDecimal(item.getQuantity());
                    BigDecimal rentalValue = new BigDecimal(item.getOptionalItem().getRentalValue());
                    return quantity.multiply(rentalValue);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Somar o valor base e o valor dos itens extras
        return valorBase.add(valorExtras);
    }

    public boolean isVehicleAvailable(Long vehicleId) {
        List<RentStatus> activeStatuses = Arrays.asList(RentStatus.IN_PROGRESS, RentStatus.PENDING);

        // Verifica se o veículo está disponível, ou seja, sem locações ativas
        return !rentRepository.isVehicleAvailable(vehicleId, activeStatuses);
    }

}
