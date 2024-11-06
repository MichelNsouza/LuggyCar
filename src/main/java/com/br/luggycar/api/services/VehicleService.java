package com.br.luggycar.api.services;

import com.br.luggycar.api.dtos.response.ClientResponse;
import com.br.luggycar.api.dtos.response.VehicleResponse;
import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.enums.rent.RentStatus;
import com.br.luggycar.api.exceptions.ResourceDatabaseException;
import com.br.luggycar.api.exceptions.ResourceExistsException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.repositories.CategoryRepository;
import com.br.luggycar.api.repositories.ClientRepository;
import com.br.luggycar.api.repositories.RentRepository;
import com.br.luggycar.api.repositories.VehicleRepository;
import com.br.luggycar.api.dtos.requests.VehicleRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RentRepository rentRepository;

    public VehicleResponse createVehicle(VehicleRequest vehicleRequest) {

        Optional<Vehicle> existingVehicle = vehicleRepository.findByPlate(vehicleRequest.getPlate());
        if (existingVehicle.isPresent()) {
            throw new ResourceExistsException("Já existe um veículo cadastrado com essa placa.");
        }

        Category category = categoryRepository.findByName(vehicleRequest.categoryName())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleRequest, vehicle);
        vehicle.setCategory(category);
        vehicle.setRegistrationDate(LocalDate.now());

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return new VehicleResponse(savedVehicle);

    }

    public List<VehicleResponse> readAllVehicle() {

        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles.stream()
                .map(VehicleResponse::new)
                .collect(Collectors.toList());

    }

    public VehicleResponse updateVehicle(Long id, VehicleRequest vehicleRequest) throws ResourceNotFoundException {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        List<RentStatus> activeStatuses = List.of(RentStatus.PENDING, RentStatus.IN_PROGRESS);

        if (rentRepository.existsByVehicleIdAndStatusIn(id,activeStatuses)) {
            throw new ResourceExistsException("veículo com aluguel em curso não pode ser editado.");
        }

        BeanUtils.copyProperties(vehicleRequest, vehicle, "id", "registrationDate");

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return new VehicleResponse(updatedVehicle);
    }

    public void deleteVehicle(Long id) {

        List<RentStatus> activeStatuses = List.of(RentStatus.PENDING, RentStatus.IN_PROGRESS);

        if (rentRepository.existsByVehicleIdAndStatusIn(id,activeStatuses)) {
            throw new ResourceExistsException("veículo com aluguel em curso não pode ser excluido.");
        }

        vehicleRepository.deleteById(id);
    }

    public Optional<VehicleResponse> findVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .map(VehicleResponse::new);
    }

    public VehicleResponse getByPlate(String plate) {
        Vehicle vehicle = vehicleRepository.findByPlate(plate)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado com a placa: " + plate));

        return new VehicleResponse(vehicle);
    }

    public List <VehicleResponse>getAvailableVehicles() {

        try {
            List<Vehicle> allVehicles = vehicleRepository.findAll();
            List<RentStatus> activeStatuses = List.of(RentStatus.PENDING, RentStatus.IN_PROGRESS);
            List<Vehicle> rentedVehicles = rentRepository.findRentedVehiclesByStatusIn(activeStatuses);
            allVehicles.removeAll(rentedVehicles);

            return allVehicles.stream()
                    .map(VehicleResponse::new)
                    .collect(Collectors.toList());

        } catch (
                ResourceDatabaseException e) {
            throw new ResourceDatabaseException("Erro ao buscar os veiculos diponiveis para aluguel no banco de dados", e);
        }
    }

    public boolean isVehicleAvailable(Long id, List<RentStatus> activeStatuses) {
            return  vehicleRepository.isVehicleAvailable(id, activeStatuses);
    }
}