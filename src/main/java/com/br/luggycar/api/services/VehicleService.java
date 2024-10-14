package com.br.luggycar.api.services;

import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.repositories.CategoryRepository;
import com.br.luggycar.api.repositories.VehicleRepository;
import com.br.luggycar.api.dtos.requests.VehicleRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private CategoryRepository categoryRepository;


    public Vehicle createVehicle(Vehicle vehicle) {
        // Verifique se a categoria existe e está sendo associada corretamente
        Category category = categoryRepository.findById(vehicle.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        vehicle.setCategory(category);
        vehicle.setRegistrationDate(LocalDate.now());
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> readAllVehicle() {


//        List <VehicleRequest> vehicleResponse = vehicleRepository.findAll();
//
//        List<Vehicle> vehicles =  new ArrayList<>();
//
//
//
//        BeanUtils.copyProperties(vehicleResponse, vehicles);
//
//
//        return vehicleResponse;

        return vehicleRepository.findAll();
    }

    public Vehicle updateVehicle(Long id, VehicleRequest vehicleRequest) {
        Optional<Vehicle> vehicle = findVehicleById(id);

        if (vehicle.isPresent()) {
            Vehicle updatedVehicle = vehicle.get();
            BeanUtils.copyProperties(vehicleRequest, updatedVehicle);
            return vehicleRepository.save(updatedVehicle);
        }

        return null;
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }


    public Optional<Vehicle> findVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

}