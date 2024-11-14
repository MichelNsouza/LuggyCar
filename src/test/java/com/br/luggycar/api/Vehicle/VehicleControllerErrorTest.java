package com.br.luggycar.api.Vehicle;

import com.br.luggycar.api.dtos.requests.VehicleRequest;
import com.br.luggycar.api.enums.vehicle.VehicleAccessorie;
import com.br.luggycar.api.enums.vehicle.VehicleColor;
import com.br.luggycar.api.enums.vehicle.VehicleManufacturer;
import com.br.luggycar.api.enums.vehicle.Vehicletransmission;
import com.br.luggycar.api.exceptions.ResourceExistsException;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.services.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class VehicleControllerErrorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    @Autowired
    private ObjectMapper objectMapper;

    private VehicleRequest vehicleRequest;

    @BeforeEach
    public void setUp() {
        vehicleRequest = new VehicleRequest(
                "Palio",
                VehicleManufacturer.FIAT,
                "flex",
                "Hatch",
                "https://www.fipe.org.br/palio",
                "ABCD1234",
                VehicleColor.BLACK,
                Vehicletransmission.MANUAL,
                "15000",
                "4",
                "450",
                Set.of(VehicleAccessorie.GPS),
                500.0
        );
    }

    @Test
    public void testCreateVehiclePlateExists() throws Exception {

        when(vehicleService.createVehicle(Mockito.any(VehicleRequest.class)))
                .thenThrow(new ResourceExistsException("Já existe um veículo cadastrado com essa placa."));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Já existe um veículo cadastrado com essa placa."));
    }

    @Test
    public void testCreateVehicleCategoryNotFound() throws Exception {

        when(vehicleService.createVehicle(Mockito.any(VehicleRequest.class)))
                .thenThrow(new ResourceExistsException("Categoria não encontrada."));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Categoria não encontrada."));
    }

    @Test
    public void testUpdateVehicleNotFound() throws Exception {
        Long nonExistentVehicleId = 999L;

        when(vehicleService.updateVehicle(Mockito.eq(nonExistentVehicleId), Mockito.any(VehicleRequest.class)))
                .thenThrow(new ResourceNotFoundException("Veículo não encontrado."));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/vehicle/{id}", nonExistentVehicleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Veículo não encontrado."));
    }

    @Test
    public void testUpdateVehicleRentInProgress() throws Exception {
        Long rentInProgress = 1L;

        when(vehicleService.updateVehicle(Mockito.eq(rentInProgress), Mockito.any(VehicleRequest.class)))
                .thenThrow(new ResourceExistsException("veículo com aluguel em curso não pode ser editado."));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/vehicle/{id}", rentInProgress)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("veículo com aluguel em curso não pode ser editado."));
    }



}
