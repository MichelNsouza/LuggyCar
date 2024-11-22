package com.br.luggycar.api.services;

import com.br.luggycar.api.dtos.response.VehicleResponse;
import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.enums.rent.RentStatus;
import com.br.luggycar.api.enums.vehicle.StatusVehicle;
import com.br.luggycar.api.exceptions.ResourceDatabaseException;
import com.br.luggycar.api.exceptions.ResourceExistsException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.repositories.CategoryRepository;
import com.br.luggycar.api.repositories.RentRepository;
import com.br.luggycar.api.repositories.VehicleRepository;
import com.br.luggycar.api.dtos.requests.VehicleRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.br.luggycar.api.configsRedis.RedisConfig.PREFIXO_VEHICLE_CACHE_REDIS;


@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public VehicleResponse createVehicle(VehicleRequest vehicleRequest) throws ResourceExistsException {

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
        vehicle.setStatusVehicle(StatusVehicle.AVAILABLE);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        redisTemplate.delete(PREFIXO_VEHICLE_CACHE_REDIS + "all_vehicles");

        return new VehicleResponse(savedVehicle);

    }


    public List<VehicleResponse> readAllVehicle() {

        List<LinkedHashMap> cachedVehiclesMap = (List<LinkedHashMap>) redisTemplate.opsForValue().get(PREFIXO_VEHICLE_CACHE_REDIS + "all_vehicles");

        if (cachedVehiclesMap != null) {

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());


            List<Vehicle> cachedVehicles = cachedVehiclesMap.stream()
                    .map(map -> mapper.convertValue(map, Vehicle.class))
                    .collect(Collectors.toList());

            return cachedVehicles.stream().map(VehicleResponse::new).collect(Collectors.toList());
        }

        List<Vehicle> vehicles = vehicleRepository.findAll();

        redisTemplate.opsForValue().set(PREFIXO_VEHICLE_CACHE_REDIS + "all_vehicles", vehicles, 3, TimeUnit.DAYS);

        return vehicles.stream().map(VehicleResponse::new).collect(Collectors.toList());
    }


    public VehicleResponse updateVehicle(Long id, VehicleRequest vehicleRequest) throws ResourceNotFoundException, ResourceExistsException {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        List<RentStatus> activeStatuses = List.of(RentStatus.PENDING, RentStatus.IN_PROGRESS);

        if (rentRepository.existsByVehicleIdAndStatusIn(id, activeStatuses)) {
            throw new ResourceExistsException("veículo com aluguel em curso não pode ser editado.");
        }

        BeanUtils.copyProperties(vehicleRequest, vehicle, "id", "registrationDate");

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        redisTemplate.delete(PREFIXO_VEHICLE_CACHE_REDIS + "all_vehicles");

        return new VehicleResponse(updatedVehicle);
    }

    public void deleteVehicle(Long id) throws ResourceExistsException, ResourceNotFoundException {

        List<RentStatus> activeStatuses = List.of(RentStatus.PENDING, RentStatus.IN_PROGRESS);

        if (rentRepository.existsByVehicleIdAndStatusIn(id, activeStatuses)) {
            throw new ResourceExistsException("veículo com aluguel em curso não pode ser excluido.");
        }
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        vehicleRepository.deleteById(id);

        redisTemplate.delete(PREFIXO_VEHICLE_CACHE_REDIS + "all_vehicles");
    }

    public Optional<VehicleResponse> findVehicleById(Long id) {

        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(id);

        if (vehicleOptional.isPresent()) {
            Vehicle vehicle = vehicleOptional.get();
            return Optional.of(new VehicleResponse(vehicle));
        }

        return Optional.empty();
    }


    public VehicleResponse getByPlate(String plate) throws ResourceNotFoundException {
        Vehicle vehicle = vehicleRepository.findByPlate(plate)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado com a placa: " + plate));

        return new VehicleResponse(vehicle);
    }


    public List<VehicleResponse> getAvailableVehicles() throws ResourceDatabaseException {

        List<Vehicle> cachedVehicles = (List<Vehicle>) redisTemplate.opsForValue().get(PREFIXO_VEHICLE_CACHE_REDIS);

        if (cachedVehicles != null) {
            return cachedVehicles.stream().map(VehicleResponse::new).collect(Collectors.toList());
        }

        List<RentStatus> activeStatuses = List.of(RentStatus.PENDING, RentStatus.IN_PROGRESS);
        List<Vehicle> availableVehicles = rentRepository.findRentedVehiclesByStatusInAndStatusVehicleAvailable(activeStatuses);

        redisTemplate.opsForValue().set(PREFIXO_VEHICLE_CACHE_REDIS + "available_vehicles", availableVehicles, 3, TimeUnit.DAYS);

        return availableVehicles.stream().map(VehicleResponse::new).collect(Collectors.toList());
    }


    public boolean isVehicleAvailableById(Long id) throws ResourceExistsException {


        try {

            List<RentStatus> activeStatuses = Arrays.asList(RentStatus.IN_PROGRESS, RentStatus.PENDING);
            boolean isAvailable = vehicleRepository.isVehicleAvailable(id, activeStatuses, StatusVehicle.AVAILABLE);

            return isAvailable;
        } catch (Exception e) {
            throw new ResourceExistsException("O veículo com ID: " + id + " possui locação em andamento, ou pendente");
        }
    }


}