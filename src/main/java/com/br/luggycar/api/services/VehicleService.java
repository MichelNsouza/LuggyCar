package com.br.luggycar.api.services;

import com.br.luggycar.api.dtos.response.ClientResponse;
import com.br.luggycar.api.dtos.response.VehicleResponse;
import com.br.luggycar.api.entities.Category;
import com.br.luggycar.api.entities.Client;
import com.br.luggycar.api.entities.Vehicle;
import com.br.luggycar.api.enums.rent.RentStatus;
import com.br.luggycar.api.enums.vehicle.StatusVehicle;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;



@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RentRepository rentRepository;
    @Autowired
    RedisTemplate redisTemplate;

    private static final String PREFIXO_CACHE_REDIS = "plate:";

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
        vehicle.setStatusVehicle(StatusVehicle.AVAILABLE);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return new VehicleResponse(savedVehicle);

    }

    public List<VehicleResponse> readAllVehicle() {
        String cacheKey = PREFIXO_CACHE_REDIS + "all_vehicles";

        List<Vehicle> cachedVehicles = (List<Vehicle>) redisTemplate.opsForValue().get(cacheKey);

        if (cachedVehicles != null) {
            return cachedVehicles.stream().map(VehicleResponse::new).collect(Collectors.toList());
        }

        List<Vehicle> vehicles = vehicleRepository.findAll();

        redisTemplate.opsForValue().set(cacheKey, vehicles, 3, TimeUnit.DAYS);

        return vehicles.stream().map(VehicleResponse::new).collect(Collectors.toList());
    }


    public VehicleResponse updateVehicle(Long id, VehicleRequest vehicleRequest) throws ResourceNotFoundException {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        List<RentStatus> activeStatuses = List.of(RentStatus.PENDING, RentStatus.IN_PROGRESS);

        if (rentRepository.existsByVehicleIdAndStatusIn(id, activeStatuses)) {
            throw new ResourceExistsException("veículo com aluguel em curso não pode ser editado.");
        }

        BeanUtils.copyProperties(vehicleRequest, vehicle, "id", "registrationDate");

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        String cacheKey = PREFIXO_CACHE_REDIS + vehicle.getPlate();
        redisTemplate.delete(cacheKey);

        return new VehicleResponse(updatedVehicle);
    }


    public void deleteVehicle(Long id) {
        List<RentStatus> activeStatuses = List.of(RentStatus.PENDING, RentStatus.IN_PROGRESS);

        if (rentRepository.existsByVehicleIdAndStatusIn(id, activeStatuses)) {
            throw new ResourceExistsException("veículo com aluguel em curso não pode ser excluido.");
        }

        // Obtém o veículo antes de deletar para poder remover o cache
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        vehicleRepository.deleteById(id);

        // Remove o cache
        String cacheKey = PREFIXO_CACHE_REDIS + vehicle.getPlate();
        redisTemplate.delete(cacheKey);
    }

    public Optional<VehicleResponse> findVehicleById(Long id) {
        String cacheKey = PREFIXO_CACHE_REDIS + "vehicle_id:" + id;

        Vehicle cachedVehicle = (Vehicle) redisTemplate.opsForValue().get(cacheKey);

        if (cachedVehicle != null) {
            return Optional.of(new VehicleResponse(cachedVehicle));
        }

        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(id);

        if (vehicleOptional.isPresent()) {
            Vehicle vehicle = vehicleOptional.get();
            redisTemplate.opsForValue().set(cacheKey, vehicle, 3, TimeUnit.DAYS);
            return Optional.of(new VehicleResponse(vehicle));
        }

        return Optional.empty();
    }


    public VehicleResponse getByPlate(String plate) {
        String cacheKey = PREFIXO_CACHE_REDIS + plate;

        Vehicle cachedVehicle = (Vehicle) redisTemplate.opsForValue().get(cacheKey); //Caso não tenha e salvar no cache

        if (cachedVehicle == null) {

        Vehicle vehicle = vehicleRepository.findByPlate(plate)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado com a placa: " + plate));

            redisTemplate.opsForValue().set(cacheKey, vehicle, 3, TimeUnit.DAYS);

            return new VehicleResponse(vehicle);
        }


        return new VehicleResponse(cachedVehicle);
    }

    public List<VehicleResponse> getAvailableVehicles() {
        String cacheKey = PREFIXO_CACHE_REDIS + "available_vehicles";

        List<Vehicle> cachedVehicles = (List<Vehicle>) redisTemplate.opsForValue().get(cacheKey);

        if (cachedVehicles != null) {
            return cachedVehicles.stream().map(VehicleResponse::new).collect(Collectors.toList());
        }

        try {
            List<RentStatus> activeStatuses = List.of(RentStatus.PENDING, RentStatus.IN_PROGRESS);
            List<Vehicle> availableVehicles = rentRepository.findRentedVehiclesByStatusInAndStatusVehicleAvailable(activeStatuses);

            redisTemplate.opsForValue().set(cacheKey, availableVehicles, 3, TimeUnit.DAYS);

            return availableVehicles.stream().map(VehicleResponse::new).collect(Collectors.toList());
        } catch (ResourceDatabaseException e) {
            throw new ResourceDatabaseException("Erro ao buscar os veículos disponíveis para aluguel no banco de dados", e);
        }
    }



    public boolean isVehicleAvailableById(Long id) {
        String cacheKey = PREFIXO_CACHE_REDIS + "availability:" + id;

        Boolean cachedAvailability = (Boolean) redisTemplate.opsForValue().get(cacheKey);

        if (cachedAvailability != null) {
            return cachedAvailability;
        }

        try {
            List<RentStatus> activeStatuses = Arrays.asList(RentStatus.IN_PROGRESS, RentStatus.PENDING);
            boolean isAvailable = vehicleRepository.isVehicleAvailable(id, activeStatuses, StatusVehicle.AVAILABLE);

            redisTemplate.opsForValue().set(cacheKey, isAvailable, 3, TimeUnit.DAYS);

            return isAvailable;
        } catch (Exception e) {
            throw new ResourceExistsException("O veículo com ID: " + id + " possui locação em andamento, ou pendente");
        }
    }



}