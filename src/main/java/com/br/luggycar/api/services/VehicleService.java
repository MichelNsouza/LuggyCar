package com.br.luggycar.api.services;

import com.br.luggycar.api.dtos.response.VehicleResponse;
import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.entities.Vehicle;
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

        Category category = categoryRepository.findById(vehicleRequest.categoryId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleRequest, vehicle);
        vehicle.setCategory(category);
        vehicle.setRegistrationDate(LocalDate.now());

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        // Implementação do retorno de um Response de veículo
        return new VehicleResponse(savedVehicle);

    }

    public List<VehicleResponse> readAllVehicle() {

        List<Vehicle> vehicles = vehicleRepository.findAll(); // Busca todos os veículos e implementa em vehicles
        return vehicles.stream() // Converto a lista em um fluxo stream, me permitindo aplicar operações como map/filter
                .map(VehicleResponse::new) // Aplica o construtor para cada elemento do fluxo
                .collect(Collectors.toList()); // coletor que converte os elementos do fluxo em uma lista novamente.

    }

    public VehicleResponse updateVehicle(Long id, VehicleRequest vehicleRequest) throws ResourceNotFoundException {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        // Atualiza as propriedades do veículo com os dados do DTO
        BeanUtils.copyProperties(vehicleRequest, vehicle, "id", "registrationDate"); // Ignora ID e data de registro

        // Salva o veículo atualizado no repositório
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        // Retorna o DTO atualizado
        return new VehicleResponse(updatedVehicle);
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }


    public Optional<VehicleResponse> findVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .map(VehicleResponse::new);
    }

}