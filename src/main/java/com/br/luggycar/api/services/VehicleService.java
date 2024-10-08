package com.br.luggycar.api.services;

import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.repositories.CategoryRepository;
import com.br.luggycar.api.repositories.VehicleRepository;
import com.br.luggycar.api.requests.VehicleRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    public List <Vehicle> getAll() {
        return vehicleRepository.findAll();
    }

    @Autowired
    private CategoryRepository categoryRepository;
    public Vehicle save(Vehicle vehicle) {
        // Verifique se a categoria existe e está sendo associada corretamente
        Category category = categoryRepository.findById(vehicle.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        vehicle.setCategory(category);
        return vehicleRepository.save(vehicle);
    }

    public Vehicle update(Long id, VehicleRequest vehicleRequest) {
        Optional<Vehicle> vehicle = findVehicleById(id);

        if (vehicle.isPresent()) {
            Vehicle updatedVehicle = vehicle.get();
            BeanUtils.copyProperties(vehicleRequest, updatedVehicle);
            return vehicleRepository.save(updatedVehicle);
        }

        return null;
    }

    public Optional<Vehicle> findVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    public void deleteVehicleById(Long id){
        vehicleRepository.deleteById(id);
    }



}