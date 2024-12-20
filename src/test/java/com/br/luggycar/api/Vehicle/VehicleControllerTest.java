package com.br.luggycar.api.Vehicle;

import com.br.luggycar.api.dtos.requests.VehicleRequest;
import com.br.luggycar.api.dtos.response.VehicleResponse;
import com.br.luggycar.api.enums.vehicle.VehicleAccessorie;
import com.br.luggycar.api.enums.vehicle.VehicleColor;
import com.br.luggycar.api.enums.vehicle.VehicleManufacturer;
import com.br.luggycar.api.enums.vehicle.Vehicletransmission;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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


    private Long id;
    private VehicleRequest vehicleRequest;
    private VehicleResponse vehicleResponse;
    private VehicleResponse vehicleResponse2;


    @BeforeEach
    public void setUp() {

        id = 1L;

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

        vehicleResponse = new VehicleResponse(
                id,
                "Palio",
                VehicleManufacturer.FIAT,
                "flex",
                "Hatch",
                "https://www.fipe.org.br/palio",
                "ABCD1234",
                VehicleColor.BLACK,
                Vehicletransmission.MANUAL,
                15000.0,
                "4",
                "450",
                Set.of(VehicleAccessorie.AR_CONDICIONADO_DIGITAL, VehicleAccessorie.GPS),
                500.0
        );

        vehicleResponse2 = new VehicleResponse(
                2L,
                "Civic",
                VehicleManufacturer.HONDA,
                "gasoline",
                "Sedan",
                "https://www.fipe.org.br/civic",
                "XYZ9876",
                VehicleColor.WHITE,
                Vehicletransmission.AUTOMATIC,
                20000.0,
                "5",
                "500",
                Set.of(VehicleAccessorie.GPS, VehicleAccessorie.AR_CONDICIONADO_DIGITAL),
                500.0
        );

    }

    @Test
    public void testCreateVehicle() throws Exception {
        Mockito.when(vehicleService.createVehicle(Mockito.any(VehicleRequest.class)))
                .thenReturn(vehicleResponse);

        String response = objectMapper.writeValueAsString(vehicleResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    public void testReadAllVehicles() throws Exception {

        Mockito.when(vehicleService.readAllVehicle()).thenReturn(List.of(vehicleResponse, vehicleResponse2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/vehicle")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Palio"))
                .andExpect(jsonPath("$[0].manufacturer").value("FIAT"))
                .andExpect(jsonPath("$[1].name").value("Civic"))
                .andExpect(jsonPath("$[1].manufacturer").value("HONDA"));
    }

    @Test
    public void testUpdateVehicle() throws Exception {
        Long id = 1L;

        VehicleRequest updatedVehicleRequest = new VehicleRequest(
                "Palio Italia", // Alteração no nome
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

        VehicleResponse updatedVehicleResponse = new VehicleResponse(
                id,
                "Palio Italia", // Alteração no nome
                VehicleManufacturer.FIAT,
                "flex",
                "Hatch",
                "https://www.fipe.org.br/palio",
                "ABCD1234",
                VehicleColor.BLACK,
                Vehicletransmission.MANUAL,
                15000.0,
                "4",
                "450",
                Set.of(VehicleAccessorie.AR_CONDICIONADO_DIGITAL,
                        VehicleAccessorie.GPS),
                500.0
        );

        Mockito.when(vehicleService.updateVehicle(eq(id), any(VehicleRequest.class))).thenReturn(updatedVehicleResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/vehicle/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedVehicleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Palio Italia"))
                .andExpect(jsonPath("$.manufacturer").value("FIAT"))
                .andExpect(jsonPath("$.version").value("flex"))
                .andExpect(jsonPath("$.categoryName").value("Hatch"))
                .andExpect(jsonPath("$.urlFipe").value("https://www.fipe.org.br/palio"))
                .andExpect(jsonPath("$.plate").value("ABCD1234"))
                .andExpect(jsonPath("$.color").value("BLACK"))
                .andExpect(jsonPath("$.currentKm").value("15000.0"))
                .andExpect(jsonPath("$.passangerCapacity").value("4"))
                .andExpect(jsonPath("$.trunkCapacity").value("450"))
                .andExpect(jsonPath("$.accessories").value(org.hamcrest.Matchers.hasItems("AR_CONDICIONADO_DIGITAL", "GPS")));
    }

    @Test
    public void testDeleteVehicle() throws Exception {
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/vehicle/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testFindVehicleById() throws Exception {

        Mockito.when(vehicleService.findVehicleById(id)).thenReturn(Optional.of(vehicleResponse));

        String response = objectMapper.writeValueAsString(vehicleResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/vehicle/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }
}


