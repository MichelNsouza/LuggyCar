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
    private RentOptionalRepository rentOptionalRepository;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private OptionalItemService optionalItemService;
    @Autowired
    private OptionalItemRepository optionalItemRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private VehicleRepository vehicleRepository;

    @Transactional
    public RentResponse createRent(RentRequest rentRequest) {


if (clientService.clientAvailable(rentRequest.clientId()) && vehicleService.isVehicleAvailableById(rentRequest.vehicleId())) {


    // Verificar disponibilidade de cliente e veículo


    // Obter detalhes do usuário autenticado e cliente/veículo
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

    // Salvar o Rent para gerar um ID e evitar erro de transiente
    rent = rentRepository.save(rent);

    // Processar e associar itens opcionais (necessita do Rent já salvo)
    List<RentOptionalItem> rentOptionalItems = optionalItemService.processAddOptionalItems(rentRequest.optionalItems(), rent);
    rent.setRentOptionalItems(rentOptionalItems);

    // Atualizar o Rent com os itens opcionais
    rentRepository.save(rent);

    // Retornar a resposta com os dados do Rent
    return new RentResponse(rent);
}
throw new ResourceBadRequestException("deu ruim");
    }

    @Transactional
    public List<RentResponse> readAllRent() {
        List<Rent> rents = rentRepository.findAll();
        return rents.stream().map(RentResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public RentResponse updateRent(Long id, RentRequestUpdate rentRequest) {
        Optional<Rent> rentOpt = rentRepository.findById(id);

        if (rentOpt.isPresent()) {
            Rent updatedRent = rentOpt.get();
            BeanUtils.copyProperties(rentRequest, updatedRent, "id");
            updatedRent.setUpdate_at(LocalDate.now());
            Rent savedRent = rentRepository.save(updatedRent);
            return new RentResponse(savedRent);
        }

        throw new ResourceNotFoundException("Rent not found with ID: " + id);
    }

    @Transactional
    public void deleteRent(Long id) {
        rentRepository.deleteById(id);
    }

    @Transactional
    public Optional<RentResponse> findRentById(Long id) {
        return rentRepository.findById(id).map(RentResponse::new);
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

//    @Transactional
//    public CloseRentalResponse closeRental(CloseRentalRequest closeRentalRequest) {
//
//        Rent rent = rentRepository.findById(closeRentalRequest.id())
//                .orElseThrow(() -> new ResourceNotFoundException("Alugel não encontrado!"));
//
//        if (rent.getStatus() != RentStatus.COMPLETED) {
//            for (RentOptionalItem rentOptionalItem : rent.getRentOptionalItems()) {
//                OptionalItem item = rentOptionalItem.getOptionalItem();
//                item.setQuantityAvailable(item.getQuantityAvailable() + rentOptionalItem.getQuantity());
//                optionalItemRepository.save(item);
//            }
//
//            // Aqui adicionar lógica para calcular o valor total, etc.
//
//            RentRequestUpdate rentRequestUpdate = new RentRequestUpdate();
//
//            rentRequestUpdate.setStatus(RentStatus.COMPLETED);
//            // rentRequestUpdate.getKmFinal() receber do close rent
//            // e mais coisas de rent
//
//
//            updateRent(rent.getId(), rentRequestUpdate);
//
//            return new CloseRentalResponse("Teste, rent fechado");
//        }
//        throw new RuntimeException("Não é possivel finalizar um aluguel ja concluido");
//    }


//passar logica de remover para ca
    // @Transactional
//    private List<RentOptionalItem> processRemoveOptionalItems(List<OptionalQuantityRequest> optionalItems, Rent rent){
//
//    }

    // @Transactional
//    private Double calcularValorFinal(Rent rent) {
//        double valorBase = rent.getVehicle().getPricePerDay() * rent.getDaysRented();
//        double valorExtras = rent.getRentOptionalItems().stream()
//                .mapToDouble(item -> item.getQuantity() * item.getOptionalItem().getPrice())
//                .sum();
//
//        return valorBase + valorExtras;
//    }
}
