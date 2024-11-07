package com.br.luggycar.api.controllers.Vehicle;

import com.br.luggycar.api.dtos.requests.VehicleRequest;
import com.br.luggycar.api.dtos.response.VehicleResponse;
import com.br.luggycar.api.enums.vehicle.VehicleAccessorie;
import com.br.luggycar.api.enums.vehicle.VehicleColor;
import com.br.luggycar.api.enums.vehicle.VehicleManufacturer;
import com.br.luggycar.api.enums.vehicle.Vehicletransmission;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateVehicle() throws Exception {

        VehicleRequest vehicleRequest = new VehicleRequest(
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

        VehicleResponse vehicleResponse = new VehicleResponse(
                1L,
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
                Set.of(VehicleAccessorie.AR_CONDICIONADO_DIGITAL,
                        VehicleAccessorie.GPS),
                500.0
        );

        Mockito.when(vehicleService.createVehicle(Mockito.any(VehicleRequest.class)))
                .thenReturn(vehicleResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Palio"))
                .andExpect(jsonPath("$.manufacturer").value("FIAT"))
                .andExpect(jsonPath("$.version").value("flex"))
                .andExpect(jsonPath("$.categoryName").value("Hatch"))
                .andExpect(jsonPath("$.urlFipe").value("https://www.fipe.org.br/palio"))
                .andExpect(jsonPath("$.plate").value("ABCD1234"))
                .andExpect(jsonPath("$.color").value("BLACK"))
                .andExpect(jsonPath("$.currentKm").value("15000"))
                .andExpect(jsonPath("$.passangerCapacity").value("4"))
                .andExpect(jsonPath("$.trunkCapacity").value("450"))
                .andExpect(jsonPath("$.accessories").value(org.hamcrest.Matchers.hasItems("AR_CONDICIONADO_DIGITAL", "GPS")));
    }

    @Test
    public void testReadAllVehicles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/vehicle")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
