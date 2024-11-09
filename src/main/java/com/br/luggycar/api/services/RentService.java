package com.br.luggycar.api.services;

import com.br.luggycar.api.dtos.response.rent.RentCreateResponse;
import com.br.luggycar.api.dtos.response.rent.RentResponse;
import com.br.luggycar.api.entities.*;
import com.br.luggycar.api.enums.rent.RentStatus;
import com.br.luggycar.api.exceptions.ResourceBadRequestException;
import com.br.luggycar.api.exceptions.ResourceDatabaseException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.repositories.*;
import com.br.luggycar.api.dtos.requests.rent.RentRequestCreate;
import com.br.luggycar.api.utils.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentService {

    @Autowired
    ClientService clientService;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private OptionalItemService optionalItemService;


    public RentCreateResponse createRent(RentRequestCreate rentRequestCreate) {
        try {

            validaVehicleAndClient(rentRequestCreate);

            String user = authUtil.getAuthenticatedUsername();
            Client client = clientRepository.findById(rentRequestCreate.client_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
            Vehicle vehicle = vehicleRepository.findById(rentRequestCreate.vehicle_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Veiculo não encontrado"));

            Rent rent = new Rent();
            BeanUtils.copyProperties(rentRequestCreate, rent);

            rent.setUser(user);
            rent.setClient(client);
            rent.setVehicle(vehicle);

            rent.setKmInitial(vehicle.getCurrentKm());
            rent.setDailyRate(vehicle.getDailyRate());
            rent.setStatus(RentStatus.IN_PROGRESS);
            rent.setExpectedCompletionDate(rent.getStartDate().plusDays(rent.getTotalDays()));

            rent = rentRepository.save(rent);

            rent.setRentOptionalItems(
                    optionalItemService.processAddOptionalItems(rentRequestCreate.optionalItems(),rent)
            );
            rent.setTotalValueOptionalItems(
                    optionalItemService.processTotalOptionalItems(rent.getRentOptionalItems())
            );
            rent.setTotalValue(
                    (vehicle.getDailyRate() * (rent.getTotalDays()))
                    + rent.getTotalValueOptionalItems()
            );
            rentRepository.save(rent);

            return new RentCreateResponse(rent);

        }catch (ResourceBadRequestException e){
            throw new ResourceBadRequestException("Algo errado na requisição! " + e.getMessage());
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

        }catch (ResourceBadRequestException e){
            throw new ResourceDatabaseException("Erro ao buscar no banco de dados", e);
        }

    }

//    @Transactional
//    public RentResponse updateRent(Long id, RentRequestUpdate rentRequest) {
//        Optional<Rent> rentOpt = rentRepository.findById(id);
//        try {
//
//            Rent updatedRent = rentOpt.get();
//
//            BeanUtils.copyProperties(rentRequest, updatedRent, "id");
//
//            updatedRent.setUpdate_at(LocalDate.now());
//
//            Rent savedRent = rentRepository.save(updatedRent);
//
//            return new RentResponse(savedRent);
//
//        }catch (ResourceBadRequestException e){
//            throw new ResourceBadRequestException("Algo deu errado! " + e.getMessage());
//        }
//    }
//
//    @Transactional
//    public void deleteRent(Long id) {
//        try {
//            rentRepository.deleteById(id);
//        }catch (ResourceDatabaseException e){
//            throw new ResourceDatabaseException("Algo deu errado!", e);
//        }
//    }

    @Transactional
    public Optional<RentResponse> findRentById(Long id) {
        try {
            return rentRepository.findById(id)
                    .map(RentResponse::new);
        }catch (ResourceNotFoundException e){
        throw new ResourceNotFoundException("Aluguel não encontrado! " + e.getMessage());
        }
    }

//    @Transactional
//    public List<RentResponse> findAllRentByClientId(Long id) {
//
//        clientService.findClientById(id);
//
//        try {
//
//            List<Rent> rents = rentRepository.findByClientId(id);
//
//            List<RentResponse> rentResponses = rents.stream()
//                    .map(rent -> {
//                        RentResponse rentResponse = new RentResponse(rent);
//                        return rentResponse;
//                    })
//                    .collect(Collectors.toList());
//
//            return rentResponses;
//
//        } catch (ResourceNotFoundException e) {
//            throw new ResourceNotFoundException("O Cliente não possui locações cadastradas! " + e.getMessage());
//        }
//    }
//
//
//    @Transactional
//    public CloseRentalResponse closeRental(RentalRequestClose rentalRequestClose) {
//
//        Rent rent = rentRepository.findById(rentalRequestClose.id())
//                .orElseThrow(() -> new ResourceNotFoundException("Aluguel não encontrado!"));
//
//        if (rent.getStatus() == RentStatus.COMPLETED) {
//            throw new RuntimeException("Não é possível finalizar um aluguel já concluído");
//        }
//
//        for (RentOptionalItem rentOptionalItem : rent.getRentOptionalItems()) {
//            OptionalItem item = rentOptionalItem.getOptionalItem();
//            item.setQuantityAvailable(item.getQuantityAvailable() + rentOptionalItem.getQuantity());
//            optionalItemRepository.save(item);
//        }
//
//        Double valorTotal = calculateFinalValue(rent);
//
//        // Calcule a multa, caso haja atraso (implementar a lógica de multa)
//        // calculateFine(valorTotal);
//
//        RentRequestUpdate rentRequestUpdate = new RentRequestUpdate();
//        rentRequestUpdate.setStatus(RentStatus.COMPLETED);
//        rentRequestUpdate.setKmFinal(rentalRequestClose.kmFinal());
//
//
//        if (rentalRequestClose.accident() != null) {
//            if (rentalRequestClose.accident().getSeverity() == Severity.HIGH
//                    || rentalRequestClose.accident().getSeverity() == Severity.MEDIUM ) {
//                Vehicle vehicle = rent.getVehicle();
//                vehicle.setStatusVehicle(StatusVehicle.UNAVAILABLE);
//                vehicleRepository.save(vehicle);
//            }
//        }
//
//        rentRequestUpdate.setTotalValue(valorTotal);
//
//        updateRent(rent.getId(), rentRequestUpdate);
//
//        return new CloseRentalResponse("Aluguel finalizado com sucesso!");
//    }
//
//
//    private double calculateFine(int atraso, Double finalValue) {
//        //fazer logica para cada tipo de atraso
//        double valorComMulta = finalValue * atraso;
//        return valorComMulta;
//    }


    public void validaVehicleAndClient(RentRequestCreate rentRequestCreate){
        clientService.clientAvailable(rentRequestCreate.client_id());
        vehicleService.isVehicleAvailableById(rentRequestCreate.vehicle_id());
    }

}
