package com.br.luggycar.api.services;

import com.br.luggycar.api.dtos.response.VehicleResponse;
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

    public VehicleResponse createVehicle(VehicleRequest vehicleRequest) {

        Category category = categoryRepository.findById(vehicleRequest.categoryId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleRequest, vehicle);
        vehicle.setCategory(category);
        vehicle.setRegistrationDate(LocalDate.now());

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
//        return vehicleRepository.save(vehicle);

        // Implementação do retorno de um Response de veículo - ver uma forma melhor
        return new VehicleResponse(
                savedVehicle.getName(),
                savedVehicle.getManufacturer(),
                savedVehicle.getVersion(),
                savedVehicle.getCategory().getId(), 
                savedVehicle.getUrlFipe(),
                savedVehicle.getPlate(),
                savedVehicle.getColor(),
                savedVehicle.getTransmission(),
                savedVehicle.getCurrentKm(),
                savedVehicle.getPassangerCapacity(),
                savedVehicle.getTrunkCapacity(),
                savedVehicle.getAccessories(),
                savedVehicle.getDailyRate()
        );

    }

    public List<Vehicle> readAllVehicle() {

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