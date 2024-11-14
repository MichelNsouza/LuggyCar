package com.br.luggycar.api.services;

import com.br.luggycar.api.dtos.requests.rent.RentRequestUpdate;
import com.br.luggycar.api.dtos.requests.rent.RentalRequestClose;
import com.br.luggycar.api.dtos.response.rent.CloseRentalResponse;
import com.br.luggycar.api.dtos.response.rent.RentCreateResponse;
import com.br.luggycar.api.dtos.response.rent.RentResponse;
import com.br.luggycar.api.dtos.response.rent.RentResponseUpdate;
import com.br.luggycar.api.entities.*;
import com.br.luggycar.api.entities.rent.Rent;
import com.br.luggycar.api.entities.rent.RentOptionalItem;
import com.br.luggycar.api.enums.accident.Severity;
import com.br.luggycar.api.enums.rent.RentStatus;
import com.br.luggycar.api.enums.vehicle.StatusVehicle;
import com.br.luggycar.api.exceptions.ResourceBadRequestException;
import com.br.luggycar.api.exceptions.ResourceDatabaseException;
import com.br.luggycar.api.exceptions.ResourceExistsException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.repositories.*;
import com.br.luggycar.api.dtos.requests.rent.RentRequestCreate;
import com.br.luggycar.api.utils.AuthUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    @Autowired
    private OptionalItemRepository optionalItemRepository;
    @Autowired
    private AccidentRepository accidentRepository;


    public RentCreateResponse createRent(RentRequestCreate rentRequestCreate) throws ResourceBadRequestException {
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

        } catch (ResourceNotFoundException | ResourceDatabaseException | ResourceExistsException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public List<RentResponse> readAllRent() throws ResourceDatabaseException {
        List<Rent> rents = rentRepository.findAll();
        return rents
                .stream()
                .map(RentResponse::new)
                .collect(Collectors.toList());

    }

    @Transactional
    public RentResponseUpdate updateRent(Long id, RentRequestUpdate rentRequestUpdate) throws ResourceBadRequestException {
        Optional<Rent> rentOpt = rentRepository.findById(id);

        Rent updatedRent = rentOpt.get();

        BeanUtils.copyProperties(rentRequestUpdate, updatedRent, "id");

        updatedRent.setUpdate_at(LocalDate.now());

        Rent savedRent = rentRepository.save(updatedRent);

        return new RentResponseUpdate(savedRent);

    }

    @Transactional
    public void deleteRent(Long id) throws ResourceDatabaseException {
        rentRepository.deleteById(id);
    }

    @Transactional
    public Optional<RentResponse> findRentById(Long id) throws ResourceNotFoundException {
        return rentRepository.findById(id)
                .map(RentResponse::new);
    }

    @Transactional
    public List<RentResponse> findAllRentByClientId(Long id) throws ResourceNotFoundException, ResourceDatabaseException {

        clientService.findClientById(id);

        List<Rent> rents = rentRepository.findByClientId(id);

        List<RentResponse> rentResponses = rents.stream()
                .map(rent -> {
                    RentResponse rentResponse = new RentResponse(rent);
                    return rentResponse;
                })
                .collect(Collectors.toList());

        return rentResponses;

    }

    public void validaVehicleAndClient(RentRequestCreate rentRequestCreate) throws ResourceDatabaseException, ResourceNotFoundException, ResourceBadRequestException, ResourceExistsException {
        clientService.clientAvailable(rentRequestCreate.client_id());
        vehicleService.isVehicleAvailableById(rentRequestCreate.vehicle_id());
    }

    @Transactional
    public CloseRentalResponse closeRental(Long id, RentalRequestClose rentalRequestClose) throws ResourceNotFoundException {

        Rent rent = rentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluguel não encontrado!"));

        if (rent.getStatus() == RentStatus.COMPLETED) {
            throw new RuntimeException("Não é possível finalizar um aluguel já concluído");
        }else {
            rent.setStatus(RentStatus.COMPLETED);
        }

        Vehicle vehicle = rent.getVehicle();
        vehicle.setCurrentKm(rentalRequestClose.kmFinal());

        rent.setKmFinal(rentalRequestClose.kmFinal());
        rent.setFinishedDate(rentalRequestClose.finishedDate());

        if (rentalRequestClose.restrictions() != null) {
            rent.getRestrictions().clear();
            rent.getRestrictions().addAll(rentalRequestClose.restrictions());
            rent.setStatus(RentStatus.PENDING);
        }

        for (RentOptionalItem rentOptionalItem : rent.getRentOptionalItems()) {
            OptionalItem item = rentOptionalItem.getOptionalItem();
            item.setQuantityAvailable(item.getQuantityAvailable() + rentOptionalItem.getQuantity());
            optionalItemRepository.save(item);
        }

        if (rentalRequestClose.accident() != null) {

            Accident accident = new Accident();
            BeanUtils.copyProperties(rentalRequestClose.accident(), accident);
            accidentRepository.save(accident);

            if (rentalRequestClose.accident().getSeverity() == Severity.HIGH
                    || rentalRequestClose.accident().getSeverity() == Severity.MEDIUM ) {
                vehicle.setStatusVehicle(StatusVehicle.UNAVAILABLE);
                vehicleRepository.save(vehicle);
            }
            rent.setStatus(RentStatus.PENDING);
        }

        Double totalPenalty = calculatePenalty(rent);

        rent.setTotalValue(finalCalculate(rent, totalPenalty));

        return new CloseRentalResponse(rent, totalPenalty );
    }

    private Double finalCalculate(Rent rent, Double totalPenalty) {
        return (rent.getDailyRate() * rent.getTotalDays()) + totalPenalty;
    }

    private Double calculatePenalty(Rent rent) {

        if (rent.getExpectedCompletionDate() != null && rent.getFinishedDate() != null) {

            long daysBetween = ChronoUnit.DAYS.between(rent.getExpectedCompletionDate(), rent.getFinishedDate());

            if (daysBetween != 0) {

                Double daily = rent.getVehicle().getDailyRate();
                Category category = rent.getVehicle().getCategory();
                Double percent = 0.0;
                Double mora = daily * 0.01;

                for (DelayPenalty penalty : category.getDelayPenalties()) {
                    if (penalty.getDays() >= daysBetween) {
                        percent = penalty.getPercentage() / 100.0;
                    }
                }

                return ((daily + (daily * percent) ) * daysBetween) * mora;
            }
            return 0.0;
        }
        return 0.0;
    }
}