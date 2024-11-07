package com.br.luggycar.api.controllers.Vehicle;

import com.br.luggycar.api.dtos.requests.VehicleRequest;
import com.br.luggycar.api.enums.vehicle.VehicleAccessorie;
import com.br.luggycar.api.enums.vehicle.VehicleColor;
import com.br.luggycar.api.enums.vehicle.VehicleManufacturer;
import com.br.luggycar.api.enums.vehicle.Vehicletransmission;
import com.br.luggycar.api.exceptions.ResourceNotFoundException;
import com.br.luggycar.api.services.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Test
    public void testCreateVehicleNotFoundException() throws Exception {

        VehicleRequest vehicleRequest = new VehicleRequest(
                "",
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

        when(vehicleService.createVehicle(Mockito.any(VehicleRequest.class)))
                .thenThrow(new ResourceNotFoundException("Veículo não encontrado"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Veículo não encontrado"));
    }
}
