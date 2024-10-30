package com.br.luggycar.api.services;

import com.br.luggycar.api.dtos.response.VehicleResponse;
import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.exceptions.ResourceExistsException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.repositories.CategoryRepository;
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

    public VehicleResponse createVehicle(VehicleRequest vehicleRequest) {

        Optional<Vehicle> existingVehicle = vehicleRepository.findByLicensePlate(vehicleRequest.getLicensePlate());
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

        BeanUtils.copyProperties(vehicleRequest, vehicle, "id", "registrationDate");

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return new VehicleResponse(updatedVehicle);
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }


    public Optional<VehicleResponse> findVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .map(VehicleResponse::new);
    }

    public VehicleResponse getByLicensePlate(String licensePlate) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado com a placa: " + licensePlate));

        return new VehicleResponse(vehicle);
    }


}