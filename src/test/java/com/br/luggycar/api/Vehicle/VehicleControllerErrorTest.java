package com.br.luggycar.api.Vehicle;

import com.br.luggycar.api.dtos.requests.VehicleRequest;
import com.br.luggycar.api.dtos.response.VehicleResponse;
import com.br.luggycar.api.enums.vehicle.VehicleAccessorie;
import com.br.luggycar.api.enums.vehicle.VehicleColor;
import com.br.luggycar.api.enums.vehicle.VehicleManufacturer;
import com.br.luggycar.api.enums.vehicle.Vehicletransmission;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.exceptions.ResourceExistsException;
import com.br.luggycar.api.services.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                "Carro Modelo X",
                VehicleManufacturer.TOYOTA,
                "flex",
                "Sedan",
                "https://www.fipe.org.br/carro-modelo-x",
                "XYZ9876",
                VehicleColor.BLACK,
                Vehicletransmission.MANUAL,
                "25000",
                "4",
                "550",
                Set.of(VehicleAccessorie.GPS),
                800.0
        );
    }

    @Test
    public void testCreateVehicleWithExistingPlate() throws Exception {
        when(vehicleService.createVehicle(any(VehicleRequest.class)))
                .thenThrow(new ResourceExistsException("Já existe um veículo cadastrado com essa placa."));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Já existe um veículo cadastrado com essa placa."));
    }

    @Test
    public void testUpdateVehicleNotFound() throws Exception {
        when(vehicleService.updateVehicle(eq(999L), any(VehicleRequest.class)))
                .thenThrow(new ResourceNotFoundException("Veículo não encontrado!"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/vehicle/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Veículo não encontrado!"));
    }

    @Test
    public void testDeleteVehicleNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Veículo não encontrado!")).when(vehicleService).deleteVehicle(eq(999L));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/vehicle/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Veículo não encontrado!"));
    }

    @Test
    public void testFindVehicleByIdNotFound() throws Exception {
        when(vehicleService.findVehicleById(eq(999L))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/vehicle/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Veículo não encontrado!"));
    }


    @Test
    public void testGetAllAvailableVehiclesNotFound() throws Exception {
        Mockito.doAnswer(invocation -> {
            throw new ResourceNotFoundException("Não há veículos disponíveis");
        }).when(vehicleService).getAvailableVehicles();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/vehicle/available"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Não há veículos disponíveis"));
    }
}








